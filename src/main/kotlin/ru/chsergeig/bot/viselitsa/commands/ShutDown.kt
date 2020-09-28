package ru.chsergeig.bot.viselitsa.commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.Permission
import ru.chsergeig.bot.viselitsa.Game
import kotlin.system.exitProcess

class ShutDown : Command() {

    init {
        name = "shutdown"
        help = "Остановить бота"
        botPermissions = arrayOf(Permission.MESSAGE_EMBED_LINKS)
        guildOnly = false
    }

    override fun execute(event: CommandEvent?) {
        Game.currentGame?.terminate()
        event?.replyWarning("Ну да, ну да. Пошел я... куда-то")
        event?.jda?.shutdown()
        exitProcess(0)
    }

}
