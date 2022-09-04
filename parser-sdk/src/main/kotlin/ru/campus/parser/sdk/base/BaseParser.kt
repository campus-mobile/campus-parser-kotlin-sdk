/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import ru.campus.parser.sdk.IParser
import ru.campus.parser.sdk.api.ParserApi
import ru.campus.parser.sdk.model.Entity
import ru.campus.parser.sdk.model.Schedule
import ru.campus.parser.sdk.model.WeekScheduleItem
import ru.campus.parser.sdk.model.ParserResult
import ru.campus.parser.sdk.model.SavedEntity
import ru.campus.parser.sdk.model.SavedSchedule
import java.util.concurrent.atomic.AtomicInteger
import java.util.logging.Level

abstract class BaseParser(
    private val parserApi: ParserApi,
    override val isLessTenEntities: Boolean = false,
) : IParser {
    protected abstract suspend fun parseInternal(): ParserResult

    suspend fun saveEntity(entity: Entity): SavedEntity {
        return parserApi.sendEntity(entity).let { result ->
            SavedEntity(entity = entity, id = result.id, status = result.status)
        }
    }

    suspend fun saveSchedule(savedEntity: SavedEntity, schedules: List<Schedule>): SavedSchedule {
        if (schedules.isEmpty()) return SavedSchedule(
            savedEntity = savedEntity,
            schedule = schedules,
            addedCount = 0,
            updatedCount = 0
        )

        return parserApi.sendSchedule(entityId = savedEntity.id, schedules = schedules).let { result ->
            SavedSchedule(
                savedEntity = savedEntity,
                schedule = schedules,
                addedCount = result.new,
                updatedCount = result.updated
            )
        }
    }

    override suspend fun parse(): ParserResult {
        return withContext(Dispatchers.Default) { parseInternal() }
    }

    protected suspend fun <T, R> parallelProcessing(
        items: List<T>,
        description: String,
        process: suspend (T) -> R,
    ): List<R> {
        val processedCounter = AtomicInteger(0)
        val itemsCount = items.size
        return coroutineScope {
            items.asFlow()
                .map { item ->
                    async {
                        try {
                            process(item)
                        } catch (exc: Exception) {
                            logger.log(Level.WARNING, "$item processing failed", exc)
                            null
                        } finally {
                            val processedCount = processedCounter.incrementAndGet()
                            logger.log(Level.INFO, "$description processed $processedCount / $itemsCount")
                        }
                    }
                }
                .buffer(10) // 10 parallel works
                .mapNotNull { it.await() }
                .toList()
        }
    }

    protected fun generateSchedules(
        currentDate: LocalDate,
        weekName: List<Schedule.Interval>.(LocalDate) -> String?,
        weekScheduleItems: List<WeekScheduleItem>,
        postprocessIntervals: (List<Schedule.Interval>) -> List<Schedule.Interval> = { it },
    ): List<Schedule> {
        if (weekScheduleItems.isEmpty()) return emptyList()

        val startOfWeek: LocalDate = currentDate.minus(DatePeriod(days = currentDate.dayOfWeek.value - 1))
        return generateSequence(startOfWeek) { it.plus(DateTimeUnit.DayBased(1)) }
            .take(28)
            .map { date ->
                val intervals: List<Schedule.Interval> = weekScheduleItems
                    .filter { it.dayCondition(date) && it.dayOfWeek == date.dayOfWeek }
                    .sortedBy { it.timeTableInterval.lessonNumber }
                    .map { item ->
                        Schedule.Interval(
                            number = item.timeTableInterval.lessonNumber,
                            start = item.timeTableInterval.startTime,
                            end = item.timeTableInterval.endTime,
                            lessons = listOf(item.lesson)
                        )
                    }
                    .let(postprocessIntervals)

                Schedule(
                    date = date.toString(),
                    week = intervals.weekName(date),
                    intervals = intervals
                )
            }
            .toList()
    }
}
