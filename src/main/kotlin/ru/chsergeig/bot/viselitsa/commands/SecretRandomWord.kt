package ru.chsergeig.bot.viselitsa.commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.Permission
import ru.chsergeig.bot.viselitsa.Game
import ru.chsergeig.bot.viselitsa.RandomWordProvider
import ru.chsergeig.bot.viselitsa.RandomWordProviderHolder
import ru.chsergeig.bot.viselitsa.exception.SansDeserializationException
import ru.chsergeig.bot.viselitsa.resources.PropertyReader.COMMAND

class SecretRandomWord : Command() {

    init {
        name = COMMAND.getByKey("randomSecret.name")
        help = COMMAND.getByKey("randomSecret.help")
        botPermissions = arrayOf(Permission.MESSAGE_EMBED_LINKS)
    }

    override fun execute(event: CommandEvent?) {
        if (Game.currentGame != null && !Game.currentGame!!.isFinished) {
            event?.replyError(COMMAND.getByKey("random.gameNotFinished"))
            return
        }
        val word: String
        try {
            word = RandomWordProvider().getWord(RandomWordProviderHolder.provider)
        } catch (e: SansDeserializationException) {
            event?.reply("Не удалось получить слово. Попробуй еще раз\n${e.message}")
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
