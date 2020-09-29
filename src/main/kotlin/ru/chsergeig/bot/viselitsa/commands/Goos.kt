package ru.chsergeig.bot.viselitsa.commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.Permission
import ru.chsergeig.bot.viselitsa.resources.PropertyReader.COMMAND
import java.util.stream.Collectors
import java.util.stream.IntStream

class Goos : Command() {

    val dict: Array<String> = arrayOf(
            """
ЗАПУСКАЕМ
░ГУСЯ░▄▀▀▀▄░РАБОТЯГИ░░
▄███▀░◐░░░▌░░░░░░░
░░░░▌░░░░░▐░░░░░░░
░░░░▐░░░░░▐░░░░░░░
░░░░▌░░░░░▐▄▄░░░░░
░░░░▌░░░░▄▀▒▒▀▀▀▀▄
░░░▐░░░░▐▒▒▒▒▒▒▒▒▀▀▄
░░░▐░░░░▐▄▒▒▒▒▒▒▒▒▒▒▀▄
░░░░▀▄░░░░▀▄▒▒▒▒▒▒▒▒▒▒▀▄
░░░░░░▀▄▄▄▄▄█▄▄▄▄▄▄▄▄▄▄▄▀▄
░░░░░░░░░░░▌▌▌▌░░░░░
░░░░░░░░░░░▌▌░▌▌░░░░░
░░░░░░░░░▄▄▌▌▄▌▌░░░░░
""",
            """
ЗАПУСКАЕМ░░
░ГУСЯ░▄▀▀▀▄░ГИДРУ░░
▄███▀░◐░▄▀▀▀▄░░░░░░
░░▄███▀░◐░░░░▌░░░
░░░▐░▄▀▀▀▄░░░▌░░░░
▄███▀░◐░░░▌░░▌░░░░
░░░░▌░░░░░▐▄▄▌░░░░░
░░░░▌░░░░▄▀▒▒▀▀▀▀▄
░░░▐░░░░▐▒▒▒▒▒▒▒▒▀▀▄
░░░▐░░░░▐▄▒▒▒▒▒▒▒▒▒▒▀▄
░░░░▀▄░░░░▀▄▒▒▒▒▒▒▒▒▒▒▀▄
░░░░░░▀▄▄▄▄▄█▄▄▄▄▄▄▄▄▄▄▄▀▄
░░░░░░░░░░░▌▌░▌▌░░░░░
░░░░░░░░░░░▌▌░▌▌░░░░░
░░░░░░░░░▄▄▌▌▄▌▌░░░░░
"""
    )

    init {
        name = COMMAND.getByKey("goose.name")
        aliases = arrayOf(COMMAND.getByKey("goose.alias.1"))
        help = COMMAND.getByKey("goose.help")
        botPermissions = arrayOf(Permission.MESSAGE_EMBED_LINKS)
        guildOnly = false
    }

    override fun execute(event: CommandEvent?) {
        val lst: MutableList<Int> = IntStream.range(0, dict.size).boxed().collect(Collectors.toList())
        lst.shuffle()
        event?.reply(dict[lst[0]])
    }

}
