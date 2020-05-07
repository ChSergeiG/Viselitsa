package ru.chsergeig.bot.viselitsa

import com.jagrosh.jdautilities.command.CommandClientBuilder
import com.jagrosh.jdautilities.examples.command.AboutCommand
import com.jagrosh.jdautilities.examples.command.ShutdownCommand
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Activity
import ru.chsergeig.bot.viselitsa.commands.AbortGame
import ru.chsergeig.bot.viselitsa.commands.GetStatus
import ru.chsergeig.bot.viselitsa.commands.Goos
import ru.chsergeig.bot.viselitsa.commands.RandomWord
import ru.chsergeig.bot.viselitsa.commands.SecretRandomWord
import ru.chsergeig.bot.viselitsa.commands.StartGame
import ru.chsergeig.bot.viselitsa.commands.SuggestChar
import java.awt.Color

fun main(args: Array<String>) {
    if (args.size != 2) {
        throw RuntimeException("Via args[] should pass token and ownerId")
    }
    val token: String = Utils.getValueFromCliArg(args, "token=", "Token")
    val ownerId: String = Utils.getValueFromCliArg(args, "ownerId=", "Owner ID")
    val client = CommandClientBuilder()
    client.setStatus(OnlineStatus.ONLINE)
    client.setActivity(Activity.listening("дичь"))
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
            AboutCommand(
                    Color.GREEN,
                    "можете звать меня просто Виселичка",
                    arrayOf("Теперь на котлине, хуле. Репо: ${Utils.url}"),
                    Permission.ADMINISTRATOR),
            ShutdownCommand())
    JDABuilder.createDefault(token)
            .setStatus(OnlineStatus.DO_NOT_DISTURB)
            .setActivity(Activity.playing("loading..."))
            .addEventListeners(client.build())
            .build()
}
