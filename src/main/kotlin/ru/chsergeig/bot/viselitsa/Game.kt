package ru.chsergeig.bot.viselitsa

import com.jagrosh.jdautilities.command.CommandEvent
import java.util.LinkedList
import java.util.stream.Collectors

class Game(word: String) {

    companion object {
        var currentGame: Game? = null
    }

    private val remainingChars: MutableList<String> = ArrayList()
    private var leftTurns = -1
    private var status: String? = null
    var isFinished = false
    private var word: String? = word.toUpperCase()

    init {
        this.leftTurns = 18
        initChars()
    }

    fun getCurrentStatus(): String {
        return "Осталось ходов: $leftTurns\nСлово: ${getMaskedWord()}\n${finalStatus()}"
    }

    fun terminate() {
        status = "Оно было рождено, чтобы умереть"
        isFinished = true
    }

    fun suggestChar(event: CommandEvent) {
        val firstChar = event.args.split("\\s+".toRegex()).toTypedArray()[0][0].toString()
        val founded = remainingChars.stream().filter { item: String -> item.contains(firstChar) }.findFirst()
        if (founded.isPresent) {
            val refined = founded.get()[0].toString()
            if (word!!.contains(refined)) {
                checkWin(event, founded.get(), refined)
            } else {
                remainingChars.remove(founded.get())
                leftTurns--
                checkFailed(event, refined)
            }
        } else {
            event.reply("Эту букву уже называли (или это вовсе не буква). А нормальные буквы завезут?")
        }
    }

    private fun initChars() {
        remainingChars.add("АаFf")
        remainingChars.add("Бб<,")
        remainingChars.add("ВвDd")
        remainingChars.add("ГгUu")
        remainingChars.add("ДдLl")
        remainingChars.add("ЕеTt")
        remainingChars.add("Жж:;")
        remainingChars.add("ЗзPp")
        remainingChars.add("ИиBb")
        remainingChars.add("ЙйQq")
        remainingChars.add("КкRr")
        remainingChars.add("ЛлKk")
        remainingChars.add("МмVv")
        remainingChars.add("НнYy")
        remainingChars.add("ОоJj")
        remainingChars.add("ПпGg")
        remainingChars.add("РрHh")
        remainingChars.add("СсCc")
        remainingChars.add("ТтNn")
        remainingChars.add("УуEe")
        remainingChars.add("ФфAa")
        remainingChars.add("Хх{[")
        remainingChars.add("ЦцWw")
        remainingChars.add("ЧчXx")
        remainingChars.add("ШшIi")
        remainingChars.add("ЩщOo")
        remainingChars.add("Ъъ}]")
        remainingChars.add("ЫыSs")
        remainingChars.add("ЬьMm")
        remainingChars.add("Ээ'\"")
        remainingChars.add("Юю>.")
        remainingChars.add("ЯяZz")
    }

    private fun finalStatus(): String? {
        return if (isFinished) status else ""

    }

    private fun getMaskedWord(): String? {
        val items: MutableList<String> = LinkedList()
        for (charr in word!!.toCharArray()) {
            val inPool = remainingChars.stream().filter { item: String -> item.contains(charr.toString() + "") }.findFirst()
            if (inPool.isPresent) {
                items.add("_")
            } else {
                items.add(charr.toString())
            }
        }
        return "```${items.stream().collect(Collectors.joining(" "))}```"
    }

    private fun checkFailed(event: CommandEvent, refined: String) {

        if (leftTurns > 0) {
            event.reply("""
Нет такой буквы ($refined)
${getMaskedWord()}
Осталось ходов: $leftTurns
Буквы, что не использованы: ${remainingChars.stream().map { e: String -> e[0].toString() }.collect(Collectors.joining(" "))}
"""
            )
        } else {
            event.reply("""
Нет такой буквы ($refined)
Ходов не осталось. Вот ты лох.
Загадано слово: $word
                    """
            )
            isFinished = true
        }
    }

    private fun checkWin(event: CommandEvent, founded: String?, refined: String) {
        for (charr in word!!.toCharArray()) {
            val inPool = remainingChars.stream().filter { item: String -> item.contains(charr.toString() + "") }.findFirst()
            if (inPool.isPresent) {
                remainingChars.remove(founded)
                if (remainingChars.stream().anyMatch { cc: String -> word!!.contains(cc[0].toString() + "") }) {
                    event.reply("""
Есть такая буква ($refined)
${getMaskedWord()}
"""
                    )
                    return
                }
                break
            }
        }
        isFinished = true
        status = "Слово отгадано целиком: ${word}. Подибил <@${event.author.id}>"
        event.reply(status)
    }
}
