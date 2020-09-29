package ru.chsergeig.bot.viselitsa.commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.Permission
import ru.chsergeig.bot.viselitsa.RandomWordProvider
import ru.chsergeig.bot.viselitsa.RandomWordProviderHolder
import ru.chsergeig.bot.viselitsa.resources.PropertyReader.COMMAND

class SetRandomWordProvider : Command() {

    init {
        name = COMMAND.getByKey("provider.name")
        help = "${COMMAND.getByKey("provider.help")} '${RandomWordProvider.Provider.values().contentToString()}'"
        botPermissions = arrayOf(Permission.MESSAGE_EMBED_LINKS)
        guildOnly = false
    }

    override fun execute(event: CommandEvent?) {
        if (event?.args!!.isEmpty()) {
            event.reply("Нужно указать вариант: '${RandomWordProvider.Provider.values().contentToString()}'")
            return
        }
        try {
            RandomWordProviderHolder.provider = RandomWordProvider.Provider.valueOf(event.args.split("\\s+".toRegex())[0])
            event.reply("Установлено ${RandomWordProviderHolder.provider}")
        } catch (e: IllegalArgumentException) {
            event.reply("Нужно указать вариант: '${RandomWordProvider.Provider.values().contentToString()}'")
        }
    }
}