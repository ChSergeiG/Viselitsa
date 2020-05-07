package ru.chsergeig.bot.viselitsa.model

class StatisticsEntry(status: Status, id: String, refined: String) {

    val status: Status = status
    val id: String = id
    val refined: String = refined

    enum class Status {
        FAIL,
        SUCCESS
    }

}
