package ru.chsergeig.bot.viselitsa.commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.Permission
import ru.chsergeig.bot.viselitsa.Game
import ru.chsergeig.bot.viselitsa.Utils.Companion.checkSingleArgAndGet
import ru.chsergeig.bot.viselitsa.resources.PropertyReader.COMMAND

class SuggestChar : Command() {

    init {
        name = COMMAND.getByKey("suggest.name")
        aliases = arrayOf(COMMAND.getByKey("suggest.alias.1"))
        help = COMMAND.getByKey("suggest.help")
        botPermissions = arrayOf(Permission.MESSAGE_WRITE)
    }

    override fun execute(event: CommandEvent?) {
        if (null == Game.currentGame) {
            event?.reply(COMMAND.getByKey("suggest.noGame"))
            return
        }
        if (!Game.currentGame!!.isFinished) {
            checkSingleArgAndGet(event!!, COMMAND.getByKey("suggest.hint.1"), COMMAND.getByKey("suggest.hint.2"))
            Game.currentGame!!.suggestChar(event)
        } else {
            event?.reply(COMMAND.getByKey("suggest.gameFinished"))
        }
    }

}
