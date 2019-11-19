package ru.skillbranch.devintensive.utils

import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    /**
     * Строку с Именем и фамилией делит на две отдельные строки
     */
    fun parseFullName(fullName: String?): Pair<String?, String?> {
        return when {
            fullName.isNullOrEmpty() -> null to null
            else -> {
                val parts: List<String>? = fullName.split(" ")
                val firstName = parts?.getOrNull(0).let {
                    if (it.isNullOrEmpty()) null else it
                }
                val lastName = parts?.getOrNull(1).let {
                    if (it.isNullOrEmpty()) null else it
                }
                firstName to lastName
            }
        }
    }

    /**
     * Из имени и фамилии получить инициалы в UpperCase
     */
    fun toInitials(firstName: String?, lastName: String?): String? {
        return when {
            firstName.getInitial() == null -> lastName.getInitial()
            lastName.getInitial() == null -> firstName.getInitial()
            else -> firstName.getInitial() + lastName.getInitial()
        }
    }

    private fun String?.getInitial(): String? {
        return when {
            this.isNullOrEmpty() || this.isBlank() -> null
            else -> this[0].toString().toUpperCase(Locale.ROOT)
        }
    }

    /**
     * Форматирует русский текст в транслит
     */
    fun transliteration(payload: String, divider: String = " "): String {
        val parts: List<String> = payload.split(" ")
        val strBuilder: StringBuilder = StringBuilder()
        for ((index, word) in parts.withIndex()) {
            strBuilder.append(word.toTransliterationFormat())
            if (index != parts.lastIndex) strBuilder.append(divider)
        }
        return strBuilder.toString()
    }

    private fun String.toTransliterationFormat(): String {
        val stringBuilder = StringBuilder()
        for (symbol in this) {
            stringBuilder.append(symbol.getTransliterationChar(symbol.isUpperCase()))
        }
        return stringBuilder.toString()
    }

    private fun Char.getTransliterationChar(isUpperCase: Boolean): String {
        return hashMapOf(
            'а' to "a",
            'б' to "b",
            'в' to "v",
            'г' to "g",
            'д' to "d",
            'е' to "e",
            'ё' to "e",
            'ж' to "zh",
            'з' to "z",
            'и' to "i",
            'й' to "i",
            'к' to "k",
            'л' to "l",
            'м' to "m",
            'н' to "n",
            'о' to "o",
            'п' to "p",
            'р' to "r",
            'с' to "s",
            'т' to "t",
            'у' to "u",
            'ф' to "f",
            'х' to "h",
            'ц' to "c",
            'ч' to "ch",
            'ш' to "sh",
            'щ' to "sh",
            'ъ' to "",
            'ы' to "i",
            'ь' to "",
            'э' to "e",
            'ю' to "yu",
            'я' to "ya"
        )[this.toLowerCase()]?.toString(isUpperCase) ?: this.toString(isUpperCase)
    }

    private fun String.toString(isUpperCase: Boolean): String {
        if (!isUpperCase) return this
        return this[0].toString().toUpperCase() + (this.getOrNull(1) ?: "")
    }

    private fun Char.toString(isUpperCase: Boolean): String {
        return if (!isUpperCase) this.toString() else this.toString().toUpperCase()
    }
}

