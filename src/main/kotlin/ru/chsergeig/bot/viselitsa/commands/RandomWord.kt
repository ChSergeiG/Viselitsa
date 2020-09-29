package ru.chsergeig.bot.viselitsa.commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.Permission
import ru.chsergeig.bot.viselitsa.Game
import ru.chsergeig.bot.viselitsa.RandomWordProvider
import ru.chsergeig.bot.viselitsa.RandomWordProviderHolder
import ru.chsergeig.bot.viselitsa.resources.PropertyReader.COMMAND

class RandomWord : Command() {

    init {
        name = COMMAND.getByKey("random.name")
        help = COMMAND.getByKey("random.help")
        botPermissions = arrayOf(Permission.MESSAGE_EMBED_LINKS)
        guildOnly = false
    }

    override fun execute(event: CommandEvent?) {
        if (Game.currentGame != null && !Game.currentGame!!.isFinished) {
            event?.replyError(COMMAND.getByKey("random.gameNotFinished"))
            return
        }
        val word = RandomWordProvider().getWord(RandomWordProviderHolder.provider)
        Game.currentGame = Game(word)
        event?.reply("""
Слово ${Game.currentGame!!.word} принято
Длина слова: ${word.length}
Число попыток: ${Game.currentGame!!.leftTurns}
""")

    }

}
