package ru.chsergeig.bot.viselitsa.commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.Permission
import ru.chsergeig.bot.viselitsa.Game
import ru.chsergeig.bot.viselitsa.resources.PropertyReader
import ru.chsergeig.bot.viselitsa.resources.PropertyReader.COMMAND

open class AbortGame : Command() {

    init {
        name = COMMAND.getByKey("abort.name")
        help = COMMAND.getByKey("abort.help")
        botPermissions = arrayOf(Permission.MESSAGE_EMBED_LINKS)
        guildOnly = false
    }

    override fun execute(event: CommandEvent?) {
        val currentGame: Game? = Game.currentGame
        if (currentGame == null) {
            event?.reply(COMMAND.getByKey("abort.noGame"))
            return
        }
        if (currentGame.isFinished) {
            event?.reply(COMMAND.getByKey("abort.gameFinished"))
            return
        }
        currentGame.terminate()
        event?.reply(COMMAND.getByKey("abort.success"))
    }

}
