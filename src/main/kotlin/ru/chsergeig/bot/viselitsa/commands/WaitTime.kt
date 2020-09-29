package ru.chsergeig.bot.viselitsa.commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.Permission
import ru.chsergeig.bot.viselitsa.WaitListHolder
import ru.chsergeig.bot.viselitsa.resources.PropertyReader.COMMAND
import java.lang.Long.parseLong

class WaitTime : Command() {

    init {
        name = COMMAND.getByKey("wait.name")
        help = COMMAND.getByKey("wait.help")
        botPermissions = arrayOf(Permission.MESSAGE_EMBED_LINKS)
        guildOnly = false
    }

    override fun execute(event: CommandEvent?) {
        if (event?.args!!.isEmpty()) {
            event.reply(COMMAND.getByKey("wait.hint.1"))
            return
        }
        try {
            WaitListHolder.setTimeToWait(parseLong(event.args.split("\\s+".toRegex())[0]))
            event.reply("Установлено ${WaitListHolder.getTimeToWait()}")
        } catch (e: NumberFormatException) {
            event.reply("${COMMAND.getByKey("wait.hint.1")}. ${e.localizedMessage}")
        }
    }

}
