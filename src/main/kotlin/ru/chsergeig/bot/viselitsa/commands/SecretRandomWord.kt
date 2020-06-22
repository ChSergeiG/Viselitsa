package ru.chsergeig.bot.viselitsa.commands

import com.fasterxml.jackson.databind.JsonMappingException
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.Permission
import ru.chsergeig.bot.viselitsa.Game
import ru.chsergeig.bot.viselitsa.RandomWordProvider
import ru.chsergeig.bot.viselitsa.RandomWordProviderHolder

class SecretRandomWord : Command() {

    init {
        name = "secret"
        help = "Использовать случайное слово, неизвестное автору"
        botPermissions = arrayOf(Permission.MESSAGE_EMBED_LINKS)
    }

    override fun execute(event: CommandEvent?) {
        if (Game.currentGame != null && !Game.currentGame!!.isFinished) {
            event?.replyError("Куда прёшь? Еще не окончена предыдущая игра")
            return
        }
        val word: String
        try {
            word = RandomWordProvider().getWord(RandomWordProviderHolder.provider)
        } catch (e: JsonMappingException) {
            event?.reply("Не получилось получить слово. Попробуй еще раз\n${e.localizedMessage}")
            return
        }
        Game.currentGame = Game(word)
        event?.reply("""
Слово принято
Длина слова: ${word.length}
Число попыток: ${Game.currentGame!!.leftTurns}
""")
    }

}
