package ru.chsergeig.bot.viselitsa

import com.jagrosh.jdautilities.command.CommandClientBuilder
import com.jagrosh.jdautilities.examples.command.AboutCommand
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Activity
import ru.chsergeig.bot.viselitsa.commands.AbortGame
import ru.chsergeig.bot.viselitsa.commands.GetStatus
import ru.chsergeig.bot.viselitsa.commands.Goos
import ru.chsergeig.bot.viselitsa.commands.RandomWord
import ru.chsergeig.bot.viselitsa.commands.SecretRandomWord
import ru.chsergeig.bot.viselitsa.commands.SetRandomWordProvider
import ru.chsergeig.bot.viselitsa.commands.ShutDown
import ru.chsergeig.bot.viselitsa.commands.StartGame
import ru.chsergeig.bot.viselitsa.commands.SuggestChar
import ru.chsergeig.bot.viselitsa.commands.WaitTime
import ru.chsergeig.bot.viselitsa.resources.PropertyReader
import java.awt.Color

class Client {

    private var token: String? = null
    private var ownerId: String? = null
    private var builder: CommandClientBuilder? = null

    fun run(args: Array<String>) {
        initArgs(args)
        builder = CommandClientBuilder()
        WaitListHolder.flush()
        initClient()
        JDABuilder.createDefault(token)
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setActivity(Activity.playing("loading..."))
                .addEventListeners(builder?.build())
                .build()
    }

    private fun initArgs(args: Array<String>) {
        token = Utils.safeGetValueFromEvOrCli(args, "token=", PropertyReader.COMMAND.getByKey("def.token"), "TOKEN")
        ownerId = Utils.safeGetValueFromEvOrCli(args, "ownerId=", PropertyReader.COMMAND.getByKey("def.oid"), "OWNER_ID")
        if (token!!.isEmpty() || ownerId!!.isEmpty()) {
            throw RuntimeException(PropertyReader.MESSAGE.getByKey("init.emptyTokenOrOwnerId"))
        }
    }

    private fun initClient() {
        builder?.setStatus(OnlineStatus.ONLINE)
        builder?.setActivity(Activity.listening(PropertyReader.MESSAGE.getByKey("bot.activity")))
        builder?.setOwnerId(ownerId)
        builder?.setEmojis("\uD83D\uDE03", "\uD83D\uDE2E", "\uD83D\uDE26")
        builder?.setPrefix("!")
        builder?.addCommands(
                StartGame(),
                GetStatus(),
                SuggestChar(),
                RandomWord(),
                SecretRandomWord(),
                AbortGame(),
                Goos(),
                ShutDown(),
                WaitTime(),
                SetRandomWordProvider(),
                AboutCommand(
                        Color.GREEN,
                        PropertyReader.MESSAGE.getByKey("bot.desc"),
                        arrayOf("Теперь на котлине, ага. Репо: ${Utils.url}"),
                        Permission.ADMINISTRATOR)
        )
    }
}
