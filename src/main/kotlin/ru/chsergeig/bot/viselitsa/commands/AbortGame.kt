package ru.chsergeig.bot.viselitsa.commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.Permission
import ru.chsergeig.bot.viselitsa.Game

class AbortGame : Command() {

    init {
        name = "abort"
        help = "Закончить игру"
        botPermissions = arrayOf(Permission.MESSAGE_EMBED_LINKS)
        guildOnly = false
    }

    override fun execute(event: CommandEvent?) {
        val currentGame: Game? = Game.currentGame
        if (currentGame == null) {
            event?.reply("Да ты ниок. Нет, что абортать. Тупц")
            return
        }
        if (currentGame.isFinished) {
            event?.reply("Я смотрю, ты не очень умен. Контрольный в голову, da?")
            return
        }
        currentGame.terminate()
        event?.reply("Потрачено")
    }

}
