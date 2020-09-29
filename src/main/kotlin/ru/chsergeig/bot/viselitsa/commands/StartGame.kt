package ru.chsergeig.bot.viselitsa.commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.Permission
import ru.chsergeig.bot.viselitsa.Game
import ru.chsergeig.bot.viselitsa.Utils.Companion.checkSingleArgAndGet
import ru.chsergeig.bot.viselitsa.resources.PropertyReader
import ru.chsergeig.bot.viselitsa.resources.PropertyReader.COMMAND

class StartGame : Command() {

    init {
        name = COMMAND.getByKey("start.name")
        help = COMMAND.getByKey("start.help")
        botPermissions = arrayOf(Permission.MESSAGE_EMBED_LINKS)
        guildOnly = false
    }

    override fun execute(event: CommandEvent?) {
        if (Game.currentGame != null && !Game.currentGame!!.isFinished) {
            event?.replyError(COMMAND.getByKey("start.gameNotFinished"))
            return
        }
        val word = checkSingleArgAndGet(
                event!!,
                COMMAND.getByKey("start.hint.1"),
                COMMAND.getByKey("start.hint.2")
        )
        Game.currentGame = Game(word)
        event.reply("""
Слово ${Game.currentGame!!.word} принято
Длина слова: ${word.length}
Число попыток: ${Game.currentGame!!.leftTurns}
""")
    }

}
