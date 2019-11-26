package ru.skillbranch.devintensive.models

import android.util.Log
import java.util.regex.Matcher

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        val (result, message) = validateInputAnswer(answer, question)
        return when (result) {
            //valid
            true -> handleAnswer(answer)
            //not valid
            false -> {
                Log.d("listenAnswer", "validateResult = $result")
                message to status.color
            }
        }
    }

    /**
     * Обработка ответа на вопрос
     */
    private fun handleAnswer(inputAnswer: String): Pair<String, Triple<Int, Int, Int>> {
        Log.d("handleAnswer", "inputAnswer = $inputAnswer")
        return if (question.answers.contains(inputAnswer)) {
            question = question.nextQuestion()
            "Отлично - ты справился\n${question.question}" to status.color
        } else {
            status = status.nextStatus()
            when (status == Status.NORMAL) {
                true -> {
                    question = Question.NAME
                    "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
                }
                false -> {
                    "Это не правильный ответ!\n" +
                            "${question.question}" to status.color
                }
            }
        }
    }

    /**
     * Проверка ввода на валидность
     */
    fun validateInputAnswer(
        inputAnswer: String,
        question: Bender.Question
    ): Pair<Boolean, String> {
        return when (question) {
            Bender.Question.NAME -> inputAnswer.validateName(
                "Имя должно начинаться с заглавной буквы\n${question.question}"
            )
            Bender.Question.PROFESSION -> inputAnswer.validateProfession(
                "Профессия должна начинаться со строчной буквы\n${question.question}"
            )
            Bender.Question.MATERIAL -> inputAnswer.validateInput(
                "^\\D+$",
                "Материал не должен содержать цифр\n${question.question}"
            )
            Bender.Question.BDAY -> inputAnswer.validateInput(
                "^\\d+$",
                "Год моего рождения должен содержать только цифры\n${question.question}"
            )
            Bender.Question.SERIAL -> inputAnswer.validateInput(
                "^\\d{7}$",
                "Серийный номер содержит только цифры, и их 7\n${question.question}"
            )
            Bender.Question.IDLE -> false to question.question//игнорировать валидацию
        }
    }

    private fun String.validateName(message: String): Pair<Boolean, String> {
        Log.d("validateName", "validateName = $this")
        return (this.isNotEmpty() && this[0].isUpperCase()) to message
    }

    private fun String.validateProfession(message: String): Pair<Boolean, String> {
        return (this.isNotEmpty() && this[0].isLowerCase()) to message
    }

    private fun String.validateInput(validateRule: String, message: String): Pair<Boolean, String> {
        return this.matches(validateRule.toRegex()) to message
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status {
            return if (this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            } else {
                values()[0]
            }
        }
    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?", listOf("Бендер", "bender")) {
            override fun nextQuestion(): Question = PROFESSION
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun nextQuestion(): Question = MATERIAL
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
            override fun nextQuestion(): Question = BDAY
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun nextQuestion(): Question = SERIAL
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun nextQuestion(): Question = IDLE
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun nextQuestion(): Question = IDLE
        };

        abstract fun nextQuestion(): Question
    }
}