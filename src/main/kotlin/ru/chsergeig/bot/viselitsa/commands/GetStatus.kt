package ru.chsergeig.bot.viselitsa.commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.Permission
import ru.chsergeig.bot.viselitsa.Game
import ru.chsergeig.bot.viselitsa.resources.PropertyReader.COMMAND

class GetStatus : Command() {

    init {
        name = COMMAND.getByKey("status.name")
        help = COMMAND.getByKey("status.help")
        botPermissions = arrayOf(Permission.MESSAGE_EMBED_LINKS)
        guildOnly = false
    }

    override fun execute(event: CommandEvent?) {
        val currentGame = Game.currentGame
        if (currentGame == null) {
            event?.reply(COMMAND.getByKey("status.noGame"))
            return
        }
        if (currentGame.isFinished) {
            event?.reply(COMMAND.getByKey("status.gameFinished"))
            return
        }
        event?.reply(currentGame.getCurrentStatus())
    }

}
