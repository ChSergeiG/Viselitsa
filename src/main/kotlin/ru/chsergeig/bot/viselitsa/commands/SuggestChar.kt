package ru.chsergeig.bot.viselitsa.commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.Permission
import ru.chsergeig.bot.viselitsa.Game
import ru.chsergeig.bot.viselitsa.Utils.Companion.checkSingleArgAndGet

class SuggestChar : Command() {

    init {
        name = "c"
        aliases = arrayOf("с")
        help = "Предложить букву"
        botPermissions = arrayOf(Permission.MESSAGE_WRITE)
    }

    override fun execute(event: CommandEvent?) {
        if (null == Game.currentGame) {
            event?.reply("Может, стоит сначала начать игру, не?")
            return
        }
        if (!Game.currentGame!!.isFinished) {
            checkSingleArgAndGet(event!!, "Понаписал то...", "Чо? Буквы не завезли чоль?")
            Game.currentGame!!.suggestChar(event)
        } else {
            event?.reply("Игра уже кончилась, ало!")
        }
    }

}
