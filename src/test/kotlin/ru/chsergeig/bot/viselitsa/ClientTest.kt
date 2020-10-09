package ru.chsergeig.bot.viselitsa

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandClientBuilder
import com.jagrosh.jdautilities.examples.command.AboutCommand
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import org.apache.commons.lang.StringUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
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
import java.util.stream.Collectors

@ExtendWith(MockitoExtension::class)
internal class ClientTest {

    companion object {

        @JvmStatic
        private var builder: CommandClientBuilder? = null

        @JvmStatic
        private var client: Client = Client()

        @BeforeAll
        @JvmStatic
        internal fun initBuilder() {
            val initMethod = Client::class.java.getDeclaredMethod("initClient")
            val ownerIdField = Client::class.java.getDeclaredField("ownerId")
            val builderField = Client::class.java.getDeclaredField("builder")
            builder = CommandClientBuilder()
            initMethod.isAccessible = true
            ownerIdField.isAccessible = true
            builderField.isAccessible = true
            builderField.set(client, builder)
            ownerIdField.set(client, "testId")
            initMethod.invoke(client)
        }
    }

    @Test
    fun initArgs() {
        val client = Client()
        val method = Client::class.java.getDeclaredMethod("initArgs", Array<String>::class.java)
        val tokenField = Client::class.java.getDeclaredField("token")
        val ownerIdField = Client::class.java.getDeclaredField("ownerId")
        method.isAccessible = true
        tokenField.isAccessible = true
        ownerIdField.isAccessible = true
        method.invoke(client, arrayOf("token=testToken", "ownerId=testId"))
        Assertions.assertTrue(StringUtils.isNotBlank(tokenField.get(client) as String))
        Assertions.assertTrue(StringUtils.isNotBlank(ownerIdField.get(client) as String))
    }

    @Test
    fun initClientStatus() {
        val statusField = CommandClientBuilder::class.java.getDeclaredField("status")
        statusField.isAccessible = true
        Assertions.assertEquals(OnlineStatus.ONLINE, statusField.get(builder))
    }

    @Test
    fun initClientActivity() {
        val activityField = CommandClientBuilder::class.java.getDeclaredField("activity")
        activityField.isAccessible = true
        Assertions.assertEquals("всякую дичь", (activityField.get(builder) as Activity).name)
        Assertions.assertEquals(Activity.ActivityType.LISTENING, (activityField.get(builder) as Activity).type)
    }

    @Test
    fun initClientOwnerId() {
        val ownerIdField = CommandClientBuilder::class.java.getDeclaredField("ownerId")
        ownerIdField.isAccessible = true
        Assertions.assertTrue(StringUtils.isNotBlank(ownerIdField.get(builder) as String))
    }

    @Test
    fun initClientEmoji() {
        val successField = CommandClientBuilder::class.java.getDeclaredField("success")
        val warningField = CommandClientBuilder::class.java.getDeclaredField("warning")
        val errorField = CommandClientBuilder::class.java.getDeclaredField("error")
        successField.isAccessible = true
        warningField.isAccessible = true
        errorField.isAccessible = true
        Assertions.assertEquals("\uD83D\uDE03", successField.get(builder))
        Assertions.assertEquals("\uD83D\uDE2E", warningField.get(builder))
        Assertions.assertEquals("\uD83D\uDE26", errorField.get(builder))
    }

    @Test
    fun initClientCommands() {
        val commandsField = CommandClientBuilder::class.java.getDeclaredField("commands")
        commandsField.isAccessible = true
        @Suppress("UNCHECKED_CAST") val commands: List<Command> =
                commandsField.get(builder) as List<Command>
        Assertions.assertEquals(11, commands.size)
        val commandClasses: List<Class<*>>? = commands
                .stream()
                .map { it.javaClass }
                .collect(Collectors.toList())
        arrayOf(
                StartGame::class.java,
                GetStatus::class.java,
                SuggestChar::class.java,
                RandomWord::class.java,
                SecretRandomWord::class.java,
                AbortGame::class.java,
                Goos::class.java,
                ShutDown::class.java,
                WaitTime::class.java,
                SetRandomWordProvider::class.java,
                AboutCommand::class.java)
                .forEach { Assertions.assertTrue(commandClasses!!.contains(it)) }
    }

}
