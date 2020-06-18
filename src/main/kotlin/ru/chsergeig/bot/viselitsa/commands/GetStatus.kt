package ru.chsergeig.bot.viselitsa.commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.Permission
import ru.chsergeig.bot.viselitsa.Game

class GetStatus : Command() {

    init {
        name = "status"
        help = "Проверить статус игры"
        botPermissions = arrayOf(Permission.MESSAGE_EMBED_LINKS)
        guildOnly = false
    }

    override fun execute(event: CommandEvent?) {

        val currentGame = Game.currentGame
        if (currentGame == null) {
            event?.reply("Ваще игры нет")
            return
        }
        if (!currentGame.isFinished) {
            event?.reply(currentGame.getCurrentStatus())
            return
        }
        event?.reply("Игра уже кончилась, ало!")
    }

}
