package ru.chsergeig.bot.viselitsa.commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.Permission
import ru.chsergeig.bot.viselitsa.Game
import ru.chsergeig.bot.viselitsa.Utils.Companion.checkSingleArgAndGet

class StartGame : Command() {

    init {
        name = "start"
        help = "Начать игру"
        botPermissions = arrayOf(Permission.MESSAGE_EMBED_LINKS)
        guildOnly = false
    }

    override fun execute(event: CommandEvent?) {
        if (Game.currentGame != null && !Game.currentGame!!.isFinished) {
            event!!.replyError("Куда прёшь? Еще не окончена предыдущая игра")
            return
        }
        val word = checkSingleArgAndGet(
                event!!,
                "Тупой, да? Нужно загадать ровно одно слово: !!start СЛОВО",
                "Слово короче твоего члена. ЕГО НЕТ, БЛЯДЬ"
        )
        Game.currentGame = Game(word)
    }

}
