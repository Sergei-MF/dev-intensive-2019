package ru.skillbranch.devintensive.extensions

import android.content.res.Resources
import java.util.regex.Pattern

/**
 *  усекает исходную строку до указанного числа символов и добавляет заполнитель "..." в конец строки
 */
fun String.truncate(length: Int = 16): String {
    if (this.length <= length) return this
    return this.substring(0 until length).let {
        it.trimEnd(' ') + "..."
    }
}

/**
 * очистка строки от лишних пробелов, html тегов, escape последовательностей
 */

private const val regExClearSpaces = "[\\s]+"
private const val regExClearHtmlGarbage = "</?[\\w='\" ]*>"
fun String.stripHtml(): String {
    return this
        .replace(regExClearSpaces.toRegex(), " ")
        .replace(regExClearHtmlGarbage.toRegex(), "")
}

/**
 *
 */
fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()