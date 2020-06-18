package ru.chsergeig.bot.viselitsa.commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.Permission
import ru.chsergeig.bot.viselitsa.Game
import ru.chsergeig.bot.viselitsa.RandomWordProvider

class RandomWord : Command() {

    init {
        name = "random"
        help = "Использовать случайное слово"
        botPermissions = arrayOf(Permission.MESSAGE_EMBED_LINKS)
        guildOnly = false
    }

    override fun execute(event: CommandEvent?) {
        if (Game.currentGame != null && !Game.currentGame!!.isFinished) {
            event?.replyError("Куда прёшь? Еще не окончена предыдущая игра")
            return
        }
        val word = RandomWordProvider().getWord(RandomWordProvider.Provider.CASTLOTS)
        Game.currentGame = Game(word)
        event?.reply("""
Слово ${Game.currentGame!!.word} принято
Длина слова: ${word.length}
Число попыток: ${Game.currentGame!!.leftTurns}
""")

    }

}
