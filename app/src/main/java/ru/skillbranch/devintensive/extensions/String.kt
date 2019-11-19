package ru.skillbranch.devintensive.extensions

/**
 *  усекает исходную строку до указанного числа символов и добавляет заполнитель "..." в конец строки
 */
fun String.truncate(length: Int = 16):String {
    if (this.length <= length) return this
    return this.substring(0 until length).let {
        it.trimEnd(' ') + "..."
    }
}