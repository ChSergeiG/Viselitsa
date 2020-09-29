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
import ru.chsergeig.bot.viselitsa.resources.PropertyReader.COMMAND
import ru.chsergeig.bot.viselitsa.resources.PropertyReader.MESSAGE
import java.awt.Color

fun main(args: Array<String>) {
    val token: String = Utils.safeGetValueFromEvOrCli(args, "token=", COMMAND.getByKey("def.token"), "TOKEN")
    val ownerId: String = Utils.safeGetValueFromEvOrCli(args, "ownerId=", COMMAND.getByKey("def.oid"), "OWNER_ID")
    val client = CommandClientBuilder()
    if (token.isEmpty() || ownerId.isEmpty()) {
        throw RuntimeException(MESSAGE.getByKey("init.emptyTokenOrOwnerId"))
    }
    WaitListHolder.flush()
    client.setStatus(OnlineStatus.ONLINE)
    client.setActivity(Activity.listening(MESSAGE.getByKey("bot.activity")))
    client.setOwnerId(ownerId)
    client.setEmojis("\uD83D\uDE03", "\uD83D\uDE2E", "\uD83D\uDE26")
    client.setPrefix("!")
    client.addCommands(
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
                    MESSAGE.getByKey("bot.desc"),
                    arrayOf("Теперь на котлине, ага. Репо: ${Utils.url}"),
                    Permission.ADMINISTRATOR)
    )
    JDABuilder.createDefault(token)
            .setStatus(OnlineStatus.DO_NOT_DISTURB)
            .setActivity(Activity.playing("loading..."))
            .addEventListeners(client.build())
            .build()
}
