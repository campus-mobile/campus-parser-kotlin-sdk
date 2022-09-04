/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parsers.spbstu

import io.ktor.client.HttpClient
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.LocalDate
import ru.campus.parser.sdk.DateProvider
import ru.campus.parser.sdk.api.ParserApi
import ru.campus.parser.sdk.api.createDefaultHttpClient
import ru.campus.parser.sdk.base.BaseParser
import ru.campus.parser.sdk.base.EntitiesCollector
import ru.campus.parser.sdk.base.ScheduleCollector
import ru.campus.parser.sdk.model.Credentials
import ru.campus.parser.sdk.model.Entity
import ru.campus.parser.sdk.model.ParserResult
import ru.campus.parser.sdk.model.ProcessedEntity
import ru.campus.parser.sdk.model.SavedEntity
import ru.campus.parser.sdk.model.SavedSchedule
import ru.campus.parser.sdk.model.Schedule
import ru.campus.parser.sdk.utils.createCombinedLogger
import ru.campus.parser.sdk.utils.createDefaultDateProvider
import ru.campus.parser.sdk.utils.getParserApiUrl
import ru.campus.parser.sdk.utils.weekName
import ru.campus.parser.sdk.utils.weekNumber
import ru.campus.parsers.spbstu.group.SPBSTUGroupEntitiesCollector
import ru.campus.parsers.spbstu.group.SPBSTUGroupScheduleCollector
import java.util.logging.Logger

/**
 * Парсер Санкт-Петербургского политехнического университета.
 * Всё, как везде, только убран коллектор интервалов из логики, раз интервалы генерируются в коллекторе расписания.
 * Список преподавателей генерируется на основне преподов в занятиях (для отзывов).
 * */
class SPBSTUParser @JvmOverloads constructor(
    credentials: Credentials,
    parserApiBaseUrl: String = getParserApiUrl(),
    override val logger: Logger = createCombinedLogger("spbstu-parser"),
    httpClient: HttpClient = createDefaultHttpClient(logger),
    parserApi: ParserApi = ParserApi(
        httpClient = httpClient,
        userName = credentials.username,
        password = credentials.password,
        baseUrl = parserApiBaseUrl
    ),
    private val dateProvider: DateProvider = createDefaultDateProvider(),
) : BaseParser(parserApi) {
    override val isWithoutSchedule: Boolean = false

    private val groupsCollector: EntitiesCollector =
        SPBSTUGroupEntitiesCollector(
            httpClient = httpClient
        )
    private val groupsScheduleCollector: ScheduleCollector =
        SPBSTUGroupScheduleCollector(
            httpClient = httpClient,
            dateProvider = dateProvider
        )

    override suspend fun parseInternal(): ParserResult {
        return coroutineScope {

            val groupsPromise = async { groupsCollector.collectEntities() }

            val groups: List<Entity> = groupsPromise.await()

            val currentDate: LocalDate = dateProvider.getCurrentDateTime().date

            val successfulGroupsPromise = async {
                parallelProcessing(groups, description = "Обработка групп") { group ->
                    processEntity(
                        scheduleCollector = groupsScheduleCollector,
                        entity = group,
                        currentDate = currentDate,
                    )
                }
            }

            val successfulGroups: List<ProcessedEntity> = successfulGroupsPromise.await()

            val teachers: List<Entity> = successfulGroups.flatMap { processedEntity ->
                val savedSchedule = processedEntity.savedSchedule
                savedSchedule.schedule.flatMap { schedule ->
                    schedule.intervals.flatMap { interval ->
                        interval.lessons.flatMap { lesson ->
                            lesson.teachers.map { teacher ->
                                Entity(
                                    name = teacher.name,
                                    code = teacher.code,
                                    type = Entity.Type.Teacher
                                )
                            }
                        }
                    }
                }
            }.distinctBy { it.code }

            val teachersForReviews: List<ProcessedEntity> = teachers.flatMap { teacher ->
                saveEntity(teacher)
                emptyList()
            }

            val successfulEntities: List<ProcessedEntity> = successfulGroups + teachersForReviews
            val savedSchedules: Sequence<SavedSchedule> = successfulEntities.asSequence().map { it.savedSchedule }

            ParserResult(
                entitiesCount = groups.size + teachersForReviews.size,
                entitiesWithLesson = successfulGroups.count { it.savedSchedule.schedule.isNotEmpty() },
                errorsCount = groups.size - successfulGroups.size,
                scheduleAddedCount = savedSchedules.sumOf { it.addedCount },
                scheduleUpdatedCount = savedSchedules.sumOf { it.updatedCount },
                savedEntities = savedSchedules.map { it.savedEntity }.toList(),
                savedSchedules = savedSchedules.toList()
            )
        }
    }

    private suspend fun processEntity(
        scheduleCollector: ScheduleCollector,
        entity: Entity,
        currentDate: LocalDate,

        ): ProcessedEntity {
        val schedule: ScheduleCollector.Result = scheduleCollector.collectSchedule(entity, emptyList())

        val savedEntity: SavedEntity = saveEntity(schedule.processedEntity)

        val processedWeekItems = schedule.weekScheduleItems

        val schedules: List<Schedule> = generateSchedules(
            currentDate = currentDate,
            weekName = { it.weekNumber.weekName },
            weekScheduleItems = processedWeekItems
        ) { it.postprocess() }
        val savedSchedule: SavedSchedule = saveSchedule(savedEntity, schedules)

        return ProcessedEntity(savedEntity, savedSchedule)
    }

    private fun List<Schedule.Interval>.postprocess(): List<Schedule.Interval> {
        return groupBy { Triple(it.number, it.start, it.end) }.map { (group, intervals) ->
            val lessons: List<Schedule.Lesson> = intervals.flatMap { it.lessons }

            Schedule.Interval(
                number = group.first,
                start = group.second,
                end = group.third,
                lessons = lessons
            )
        }
    }
}
