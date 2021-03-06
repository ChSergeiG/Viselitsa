package ru.chsergeig.bot.viselitsa

import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.entities.EmbedType
import net.dv8tion.jda.api.entities.MessageEmbed
import ru.chsergeig.bot.viselitsa.model.StatisticsEntry
import ru.chsergeig.bot.viselitsa.resources.PropertyReader.MESSAGE
import java.time.OffsetDateTime
import java.util.LinkedList
import java.util.Optional
import java.util.stream.Collectors
import kotlin.collections.set

class Game(word: String, var leftTurns: Int) {

    companion object {
        var currentGame: Game? = null
    }

    constructor(word: String) : this(word, -1)

    private val dictionary: MutableMap<String, Boolean> = LinkedHashMap()
    private val statistics: MutableList<StatisticsEntry> = ArrayList()
    private var status: String? = null
    var isFinished = false
    private var originWord: String?
    var word: String?

    init {
        if (leftTurns == -1) {
            val limit = 15 - word.toCollection(HashSet()).size * 2 / 3
            leftTurns = if (limit < 5) 5 else limit
        }
        initChars()
        this.originWord = word
        this.word = Utils.purifyWord(word.toUpperCase(), dictionary)
        if (this.word!!.isEmpty()) {
            isFinished = true
        }
        WaitListHolder.flush()
    }

    fun getCurrentStatus(): String {
        return """
Осталось попыток: $leftTurns
Слово: ${getMaskedWord()}
Буквы: ${getMaskedDict()}
${finalStatus()}
"""
    }

    fun terminate() {
        status = MESSAGE.getByKey("bot.game.terminateMessage")
        isFinished = true
    }

    fun suggestChar(event: CommandEvent) {
        if (!checkUserNotWaits(event)) {
            return
        }
        addUserToWaitList(event)
        val firstChar = event.args.split("\\s+".toRegex()).toTypedArray()[0][0].toString()
        val foundedInDictionary = findOptEntryInDict(firstChar)
        if (foundedInDictionary.isPresent && !foundedInDictionary.get().value) {
            val keyValue = foundedInDictionary.get().key
            val refined = keyValue[0].toString()
            dictionary[keyValue] = true
            if (word!!.contains(refined)) {
                addStatistics(StatisticsEntry.Status.SUCCESS, event, refined)
                checkWin(event, refined)
            } else {
                addStatistics(StatisticsEntry.Status.FAIL, event, refined)
                leftTurns--
                checkFailed(event, refined)
            }
        } else {
            event.reply(MESSAGE.getByKey("bot.game.wrongSymbol"))
        }
    }

    private fun checkUserNotWaits(event: CommandEvent): Boolean {
        if (WaitListHolder.checkWaits(event.author.id)) {
            event.reply("<@${event.author.id}> Не так быстро, петушок")
            return false
        }
        return true
    }

    private fun addUserToWaitList(event: CommandEvent) {
        WaitListHolder.add(event.author.id)
    }

    private fun addStatistics(status: StatisticsEntry.Status, event: CommandEvent, refined: String) {
        statistics.add(StatisticsEntry(status, event.author.id, refined))
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
Осталось попыток: $leftTurns
Буквы: ${getMaskedDict()}
"""
            )
        } else {
            status = """
Нет такой буквы ($refined)
Ходов не осталось. Спасибо, <@${event.author?.id}>, за просранную игру.
Загадано слово: $word [$originWord]. [Значение](http://gramota.ru/slovari/dic/?bts=x&word=${originWord})
Стата:
${getSummary()}
"""
            isFinished = true
            val messageEmbed = MessageEmbed(
                    null,
                    "Игра окончена",
                    status,
                    EmbedType.UNKNOWN,
                    OffsetDateTime.now(),
                    0xCB2710,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            )
            event.reply(messageEmbed)
        }
    }

    private fun checkWin(event: CommandEvent, refined: String) {
        if (word!!.toCharArray()
                        .map { charr: Char -> findOptEntryInDict(charr.toString()) }
                        .all { opt -> opt.get().value }
        ) {
            isFinished = true
            status = """
Слово отгадано целиком: $word [$originWord]. [Значение](http://gramota.ru/slovari/dic/?bts=x&word=${originWord})
Подибил <@${event.author?.id}>
Стата:
${getSummary()}
"""
            val messageEmbed = MessageEmbed(
                    null,
                    "Игра окончена",
                    status,
                    EmbedType.UNKNOWN,
                    OffsetDateTime.now(),
                    0x1BA270,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            )
            event.reply(messageEmbed)
        } else {
            event.reply("""
Есть такая буква ($refined)
${getMaskedWord()}
Осталось попыток: $leftTurns
Буквы: ${getMaskedDict()}
"""
            )
        }
    }

    private fun getSummary(): String {
        return statistics.stream()
                .map { entry -> entry.id }
                .distinct()
                .map { id ->
                    "<@$id> отгадал ${
                        statistics.stream()
                                .filter { entry -> entry.id == id && entry.status == StatisticsEntry.Status.SUCCESS }
                                .count()
                    } букв за ${
                        statistics.stream()
                                .filter { entry -> entry.id == id }
                                .count()
                    } попыток [${
                        statistics.stream()
                                .filter { entry -> entry.id == id && entry.status == StatisticsEntry.Status.SUCCESS }
                                .map { entry -> entry.refined }
                                .collect(Collectors.joining(""))
                    }/${
                        statistics.stream()
                                .filter { entry -> entry.id == id && entry.status == StatisticsEntry.Status.FAIL }
                                .map { entry -> entry.refined }
                                .collect(Collectors.joining(""))
                    }]"
                }.collect(Collectors.joining("\n"))
    }

    private fun findOptEntryInDict(suggest: String): Optional<MutableMap.MutableEntry<String, Boolean>> {
        return dictionary.entries
                .stream()
                .filter { entry: MutableMap.MutableEntry<String, Boolean> -> entry.key.contains(suggest) }
                .findFirst()
    }

}
