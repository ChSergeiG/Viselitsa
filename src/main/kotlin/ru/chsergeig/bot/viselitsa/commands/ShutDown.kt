package ru.chsergeig.bot.viselitsa.commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.Permission
import kotlin.system.exitProcess

class ShutDown : Command() {

    init {
        name = "shutdown"
        help = "Остановить бота"
        botPermissions = arrayOf(Permission.MESSAGE_EMBED_LINKS)
        guildOnly = false
    }

    override fun execute(event: CommandEvent?) {
        event!!.reactWarning()
        event.jda.shutdown()
        exitProcess(0)
    }

}
