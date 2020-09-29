package ru.chsergeig.bot.viselitsa.commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.Permission
import ru.chsergeig.bot.viselitsa.Game
import ru.chsergeig.bot.viselitsa.resources.PropertyReader.COMMAND
import kotlin.system.exitProcess

class ShutDown : Command() {

    init {
        name = COMMAND.getByKey("shutdown.name")
        help = COMMAND.getByKey("shutdown.help")
        botPermissions = arrayOf(Permission.MESSAGE_EMBED_LINKS)
        guildOnly = false
    }

    override fun execute(event: CommandEvent?) {
        Game.currentGame?.terminate()
        event?.replyWarning(COMMAND.getByKey("shutdown.success"))
        event?.jda?.shutdown()
        exitProcess(0)
    }

}
