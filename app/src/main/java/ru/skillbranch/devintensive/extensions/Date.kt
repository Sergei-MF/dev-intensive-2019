package ru.skillbranch.devintensive.extensions

import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

/**
 * Форматирование даты по заданному паттерну
 */
fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    return SimpleDateFormat(pattern, Locale("ru")).format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    this.time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    return this
}

enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY
}

/**
 * возвращает значение в виде строки с правильно склоненной единицой измерения
 */
fun TimeUnits.plural(value: Int): String {
    val lastSymbol =
        if (value.toString().endsWith("11")) 11 else value.toString().last().toString().toInt()

    return when (lastSymbol) {
        0, in 5..9, 11 -> {
            when (this) {
                TimeUnits.SECOND -> "$value секунд"
                TimeUnits.MINUTE -> "$value минут"
                TimeUnits.HOUR -> "$value часов"
                TimeUnits.DAY -> "$value дней"
            }
        }
        1 -> {
            when (this) {
                TimeUnits.SECOND -> "$value секунду"
                TimeUnits.MINUTE -> "$value минуту"
                TimeUnits.HOUR -> "$value час"
                TimeUnits.DAY -> "$value день"
            }
        }
        2, 3, 4 -> {
            when (this) {
                TimeUnits.SECOND -> "$value секунды"
                TimeUnits.MINUTE -> "$value минуты"
                TimeUnits.HOUR -> "$value часа"
                TimeUnits.DAY -> "$value дня"
            }
        }
        else -> "хз что за символ = $lastSymbol"
    }
}

/**
 * форматирования вывода разницы между текущим экземпляром Date и текущим моментом времени
 * (или указанным в качестве аргумента) в человекообразном формате
 */
fun Date.humanizeDiff(date: Date = Date()): String {
    val difference: Long = this.time - date.time
    return when {
        difference < 0 -> humanizePast(abs(difference))
        else -> humanizeFuture(difference)
    }
}

private fun humanizeFuture(time: Long): String {
    return when (time) {
        in 0 until SECOND -> "только что"
        in SECOND until 45 * SECOND -> "через несколько секунд"
        in 45 * SECOND until 75 * SECOND -> "через минуту"
        in 75 * SECOND until 45 * MINUTE -> "через ${TimeUnits.MINUTE.plural((time / MINUTE).toInt())}"
        in 45 * MINUTE until 75 * MINUTE -> "через час"
        in 75 * MINUTE until 22 * HOUR -> "через ${TimeUnits.HOUR.plural((time / HOUR).toInt())}"
        in 22 * HOUR until 26 * HOUR -> "через день"
        in 26 * HOUR until 360 * DAY -> "через ${TimeUnits.DAY.plural((time / DAY).toInt())}"
        else -> "более чем через год"
    }
}

private fun humanizePast(time: Long): String {
    return when (time) {
        in 0 until SECOND -> "только что"
        in SECOND until 45 * SECOND -> "несколько секунд назад"
        in 45 * SECOND until 75 * SECOND -> "минуту назад"
        in 75 * SECOND until 45 * MINUTE -> "${TimeUnits.MINUTE.plural((time / MINUTE).toInt())} назад"
        in 45 * MINUTE until 75 * MINUTE -> "час назад"
        in 75 * MINUTE until 22 * HOUR -> "${TimeUnits.HOUR.plural((time / HOUR).toInt())} назад"
        in 22 * HOUR until 26 * HOUR -> "день назад"
        in 26 * HOUR until 360 * DAY -> "${TimeUnits.DAY.plural((time / DAY).toInt())} назад"
        else -> "более года назад"
    }
}