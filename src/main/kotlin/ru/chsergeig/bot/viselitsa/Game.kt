package ru.chsergeig.bot.viselitsa

import com.jagrosh.jdautilities.command.CommandEvent
import java.util.LinkedList
import java.util.Optional
import java.util.stream.Collectors

class Game(word: String) {

    companion object {
        var currentGame: Game? = null
    }

    private val dictionary: MutableMap<String, Boolean> = LinkedHashMap()
    private var leftTurns = -1
    private var status: String? = null
    var isFinished = false
    var word: String? = word.toUpperCase()

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
        val foundedInDictionary = findOptEntryInDict(firstChar)
        if (foundedInDictionary.isPresent && !foundedInDictionary.get().value) {
            val keyValue = foundedInDictionary.get().key
            val refined = keyValue[0].toString()
            dictionary[keyValue] = true
            if (word!!.contains(refined)) {
                checkWin(event, keyValue, refined)
            } else {
                leftTurns--
                checkFailed(event, refined)
            }
        } else {
            event.reply("Эту букву уже называли (или это вовсе не буква). А нормальные буквы завезут?")
        }
    }

    private fun initChars() {
        dictionary["АаFf"] = false
        dictionary["Бб<,"] = false
        dictionary["ВвDd"] = false
        dictionary["ГгUu"] = false
        dictionary["ДдLl"] = false
        dictionary["ЕеTt"] = false
        dictionary["Ёё`~"] = false
        dictionary["Жж:;"] = false
        dictionary["ЗзPp"] = false
        dictionary["ИиBb"] = false
        dictionary["ЙйQq"] = false
        dictionary["КкRr"] = false
        dictionary["ЛлKk"] = false
        dictionary["МмVv"] = false
        dictionary["НнYy"] = false
        dictionary["ОоJj"] = false
        dictionary["ПпGg"] = false
        dictionary["РрHh"] = false
        dictionary["СсCc"] = false
        dictionary["ТтNn"] = false
        dictionary["УуEe"] = false
        dictionary["ФфAa"] = false
        dictionary["Хх{["] = false
        dictionary["ЦцWw"] = false
        dictionary["ЧчXx"] = false
        dictionary["ШшIi"] = false
        dictionary["ЩщOo"] = false
        dictionary["Ъъ}]"] = false
        dictionary["ЫыSs"] = false
        dictionary["ЬьMm"] = false
        dictionary["Ээ'\""] = false
        dictionary["Юю>."] = false
        dictionary["ЯяZz"] = false
    }

    private fun finalStatus(): String? {
        return if (isFinished) status else ""

    }

    private fun getMaskedWord(): String? {
        val items: MutableList<String> = LinkedList()
        for (charr in word!!.toCharArray()) {
            val dictEntry = findOptEntryInDict(charr.toString())
            if (dictEntry.isPresent) {
                items.add(if (dictEntry.get().value) dictEntry.get().key[0].toString() else "_")
            }
        }
        return "```${items.stream().collect(Collectors.joining(" "))}```"
    }

    private fun getMaskedDict(): String {
        return dictionary.entries
                .stream()
                .map { entry: MutableMap.MutableEntry<String, Boolean> -> if (entry.value) "\u2588" else entry.key[0].toString() }
                .collect(Collectors.joining(" "))
                .trim()
    }

    private fun checkFailed(event: CommandEvent, refined: String) {

        if (leftTurns > 0) {
            event.reply("""
Нет такой буквы ($refined)
${getMaskedWord()}
Осталось ходов: $leftTurns
Буквы, что не использованы: ${getMaskedDict()}
"""
            )
        } else {
            event.reply("""
Нет такой буквы ($refined)
Ходов не осталось.
Загадано слово: $word
                    """
            )
            isFinished = true
        }
    }

    private fun checkWin(event: CommandEvent, founded: String?, refined: String) {
        if (word!!
                        .toCharArray()
                        .map { charr: Char -> findOptEntryInDict(charr.toString()) }
                        .all { opt -> opt.get().value }
        ) {
            isFinished = true
            status = "Слово отгадано целиком: ${word}. Подибил <@${event.author?.id}>"
            event.reply(status)
        } else {
            event.reply("""
Есть такая буква ($refined)
${getMaskedWord()}
Еще не использованы ${getMaskedDict()}
"""
            )
        }
    }

    private fun findOptEntryInDict(suggest: String): Optional<MutableMap.MutableEntry<String, Boolean>> {
        return dictionary.entries
                .stream()
                .filter { entry: MutableMap.MutableEntry<String, Boolean> -> entry.key.contains(suggest) }
                .findFirst()
    }

}
