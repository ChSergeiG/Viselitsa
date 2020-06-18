package ru.chsergeig.bot.viselitsa.commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.Permission
import ru.chsergeig.bot.viselitsa.WaitList
import java.lang.Long.parseLong
import java.lang.NumberFormatException

class WaitTime : Command() {

    init {
        name = "wait"
        help = "Установить ожидание игроков"
        botPermissions = arrayOf(Permission.MESSAGE_EMBED_LINKS)
        guildOnly = false
    }

    override fun execute(event: CommandEvent?) {
        if (event?.args!!.isEmpty()) {
            event.reply("Тупица. Нужно ожидание в миллисекундах указать какбе")
            return
        }
        try {
            WaitList.setTimeToWait(parseLong(event.args.split("\\s+".toRegex())[0]))
            event.reply("Установлено ${WaitList.getTimeToWait()}")
        } catch (e: NumberFormatException) {
            event.reply("Тупица. Нужно ожидание в миллисекундах указать какбе. ${e.localizedMessage}")
            return
        }
    }

}
