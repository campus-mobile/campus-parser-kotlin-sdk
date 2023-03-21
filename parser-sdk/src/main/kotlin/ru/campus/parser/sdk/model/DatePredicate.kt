/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.model

import kotlinx.datetime.LocalDate
import ru.campus.parser.sdk.utils.weekNumber

abstract class DatePredicate {
    abstract operator fun invoke(date: LocalDate): Boolean

    abstract val stringDescription: String

    override fun toString(): String {
        return "DatePredicate($stringDescription)"
    }

    override fun equals(other: Any?): Boolean {
        return stringDescription == (other as? DatePredicate)?.stringDescription
    }

    override fun hashCode(): Int {
        return stringDescription.hashCode()
    }
}

class AllWeeksDatePredicate : DatePredicate() {
    override fun invoke(date: LocalDate): Boolean = true

    override val stringDescription: String = "all-weeks"
}

class EvenWeeksDatePredicate(
    private val getWeekNumber: (LocalDate) -> Int = { it.weekNumber },
) : DatePredicate() {
    override fun invoke(date: LocalDate): Boolean = getWeekNumber(date) % 2 == 0

    override val stringDescription: String = "even-weeks"
}

class OddWeeksDatePredicate(
    private val getWeekNumber: (LocalDate) -> Int = { it.weekNumber },
) : DatePredicate() {
    override fun invoke(date: LocalDate): Boolean = getWeekNumber(date) % 2 != 0

    override val stringDescription: String = "odd-weeks"
}

class ExplicitWeeksDatePredicate(
    private val weekNumbers: List<Int>,
    private val getWeekNumber: (LocalDate) -> Int = { it.weekNumber },
) : DatePredicate() {
    override fun invoke(date: LocalDate): Boolean = getWeekNumber(date).let { weekNumbers.contains(it) }

    override val stringDescription: String = "explicit-weeks-$weekNumbers"
}

class ExplicitDatePredicate(
    private val date: LocalDate,
) : DatePredicate() {
    override fun invoke(date: LocalDate): Boolean = date == this.date

    override val stringDescription: String = "explicit-date-$date"
}

class MinimalDatePredicate(
    private val minDate: LocalDate
) : DatePredicate() {
    override fun invoke(date: LocalDate): Boolean {
        return minDate <= date
    }

    override val stringDescription: String
        get() = "min-date-$minDate"
}

class MaximumDatePredicate(
    private val maxDate: LocalDate
) : DatePredicate() {
    override fun invoke(date: LocalDate): Boolean {
        return maxDate >= date
    }

    override val stringDescription: String
        get() = "max-date-$maxDate"
}

@Deprecated(message = "use AllOfDatePredicate", replaceWith = ReplaceWith("AllOfDatePredicate"))
class CombinedDatePredicate(
    private val predicates: List<DatePredicate>
) : DatePredicate() {
    override fun invoke(date: LocalDate): Boolean {
        return predicates.all { it.invoke(date) }
    }

    override val stringDescription: String get() = predicates.toString()
}

class AllOfDatePredicate(
    private val predicates: List<DatePredicate>
) : DatePredicate() {
    override fun invoke(date: LocalDate): Boolean {
        return predicates.all { it.invoke(date) }
    }

    override val stringDescription: String get() = "all:$predicates"
}

class AnyOfDatePredicate(
    private val predicates: List<DatePredicate>
) : DatePredicate() {
    override fun invoke(date: LocalDate): Boolean {
        return predicates.any { it.invoke(date) }
    }

    override val stringDescription: String get() = "any:$predicates"
}
