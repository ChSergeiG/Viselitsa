package ru.chsergeig.bot.viselitsa.model

class StatisticsEntry(val status: Status, val id: String, val refined: String) {

    enum class Status {
        FAIL,
        SUCCESS
    }

}
