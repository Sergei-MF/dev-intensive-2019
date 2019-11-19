package ru.skillbranch.devintensive.models

import java.util.*

abstract class BaseMessage(
    val id: String,
    val from: User?,
    val chat: Chat,
    val isIncoming: Boolean = false,
    val date: Date = Date()
) {

    abstract fun foormatMessage(): String

    companion object AbstractFactory {
        var lastId = -1
        fun makeMessage(
            from: User?,
            chat: Chat,
            date: Date = Date(),
            type: String = "text",
            paylod: Any?
        ): BaseMessage {
            lastId++
            return when (type) {
                "image" -> ImageMessage(
                    "$lastId",
                    from,
                    chat,
                    date = date,
                    image = paylod as String
                )
                else -> TextMessage("$lastId", from, chat, date = date, text = paylod as String)
            }
        }
    }
}