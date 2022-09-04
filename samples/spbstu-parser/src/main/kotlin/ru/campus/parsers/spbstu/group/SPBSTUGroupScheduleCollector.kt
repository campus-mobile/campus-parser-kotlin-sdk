/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parsers.spbstu.group

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ru.campus.parser.sdk.DateProvider
import ru.campus.parser.sdk.base.ScheduleCollector
import ru.campus.parser.sdk.model.Entity
import ru.campus.parser.sdk.model.ExplicitDatePredicate
import ru.campus.parser.sdk.model.Schedule
import ru.campus.parser.sdk.model.TimeTableInterval
import ru.campus.parser.sdk.model.WeekScheduleItem
import ru.campus.parsers.spbstu.model.SpbstuAuditory
import ru.campus.parsers.spbstu.model.SpbstuBuilding
import ru.campus.parsers.spbstu.model.SpbstuLesson
import ru.campus.parsers.spbstu.model.SpbstuSchedule
import java.net.URL
import java.time.LocalDate
import kotlinx.datetime.LocalDate as kotlinLocalDate

/**
 * Коллектор расписания Санкт-Петербургского политехнического университета.
 * Собираем расписание на четыре недели, начиная с текущей даты. Генерируем ссылку на каждую неделю отдельно, получаем
 * содержимое страницы. Пришлось добавить обработку интервалов занятий для каждой недели отдельно, потому что они меняются
 * и часто у разных групп разные. Плюс на экзаменах-зачетах-практиках интервалы сильно отличаются. Поэтому формируем список
 * интервалов для каждого дня. Дальше просто поэлементно разбираем каждый интервал и получаем из него всю возможную инфу.
 * */
class SPBSTUGroupScheduleCollector(
    private val httpClient: HttpClient,
    private val dateProvider: DateProvider,
    private val json: Json = Json { ignoreUnknownKeys = true },
) : ScheduleCollector {
    private val scheduleApi: String = "https://ruz.spbstu.ru/api/v1/ruz/scheduler/"

    override suspend fun collectSchedule(entity: Entity, intervals: List<TimeTableInterval>): ScheduleCollector.Result {
        val weekScheduleItems: MutableList<WeekScheduleItem> = mutableListOf()
        var date: LocalDate = dateProvider.getCurrentDateTime().date.toJavaLocalDate()
        for (i in 0 until 4) {
            val url: String = scheduleApi + entity.code + "?date=$date"
            val scheduleString: String = httpClient.get(url)
            val daySchedule: SpbstuSchedule = json.decodeFromString(scheduleString)
            weekScheduleItems.addAll(
                daySchedule.days.flatMap { day ->
                    val lessonDate: kotlinLocalDate = LocalDate.parse(day.date).toKotlinLocalDate()
                    val dayOfWeek: DayOfWeek = lessonDate.dayOfWeek
                    // сгенерируем предварительно список интервалов
                    val dayIntervals: List<TimeTableInterval> = day.lessons.map { lesson ->
                        val intervalStart: String = lesson.intervalStart
                        val intervalEnd: String = lesson.intervalEnd
                        Pair(intervalStart, intervalEnd)
                    }.distinct().mapIndexed { index: Int, interval ->
                        TimeTableInterval(index + 1, interval.first, interval.second)
                    }
                    day.lessons.map { lesson ->
                        WeekScheduleItem(
                            dayOfWeek = dayOfWeek,
                            timeTableInterval = dayIntervals.first { it.startTime == lesson.intervalStart && it.endTime == lesson.intervalEnd },
                            dayCondition = ExplicitDatePredicate(lessonDate),
                            lesson = processLesson(lesson)
                        )
                    }
                })
            date = date.plusDays(7)
        }
        return ScheduleCollector.Result(
            processedEntity = entity,
            weekScheduleItems = weekScheduleItems
        )
    }

    private fun processLesson(lesson: SpbstuLesson): Schedule.Lesson {
        val audAndBuilding: SpbstuAuditory? = lesson.auditory?.first()
        val building: SpbstuBuilding? = audAndBuilding?.building
        val linksList: MutableList<Schedule.Link> = mutableListOf()
        val webinarLink: String? = runCatching { URL(lesson.webinarLink) }.getOrNull()?.toString()
        if (webinarLink != null) {
            linksList.add(Schedule.Link(title = "Вебинар", url = webinarLink))
        }
        val lmsLink: String? = runCatching { URL(lesson.lmsLink) }.getOrNull()?.toString()
        if (lmsLink != null) {
            linksList.add(Schedule.Link(title = "СДО", url = lmsLink))
        }
        return Schedule.Lesson(
            subject = lesson.subject,
            comment = lesson.comment?.takeIf { it.isNotEmpty() },
            type = lesson.lessonParams.typeName,
            classroom = audAndBuilding?.name,
            building = Schedule.Building(
                name = building?.name,
                address = building?.address?.takeIf { it.isNotEmpty() },
                coordinate = null
            ),
            teachers = lesson.teachers?.map { teacher ->
                Schedule.Entity(name = teacher.name, code = teacher.code.toString())
            } ?: emptyList(),
            links = linksList
        )
    }
}
