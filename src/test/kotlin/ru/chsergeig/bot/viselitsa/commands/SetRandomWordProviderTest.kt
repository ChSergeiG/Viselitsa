package ru.chsergeig.bot.viselitsa.commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.Permission
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import ru.chsergeig.bot.viselitsa.Game
import ru.chsergeig.bot.viselitsa.RandomWordProvider
import ru.chsergeig.bot.viselitsa.UtilsForTests
import ru.chsergeig.bot.viselitsa.resources.PropertyReader

@ExtendWith(MockitoExtension::class)
internal class SetRandomWordProviderTest {

    @Captor
    var captor: ArgumentCaptor<String>? = null

    @AfterEach
    fun afterEach() {
        Game.currentGame = null
    }

    @Test
    fun constructCommand() {
        val command: Command = SetRandomWordProvider()
        UtilsForTests.testCommand(
                command,
                PropertyReader.COMMAND.getByKey("provider.name"),
                arrayListOf(),
                "${PropertyReader.COMMAND.getByKey("provider.help")} '${RandomWordProvider.Provider.values().contentToString()}'",
                arrayOf(Permission.MESSAGE_EMBED_LINKS),
                1,
                isGuildOnly = false,
                isHidden = false)
    }

    @Test
    fun execute1(@Mock event: CommandEvent) {
        val command: Command = SetRandomWordProvider()
        Mockito.`when`(event.args).thenReturn("")

        val method = Command::class.java.getDeclaredMethod("execute", CommandEvent::class.java)
        method.isAccessible = true
        method.invoke(command, event)

        Mockito.verify(event).reply(captor?.capture())
        Assertions.assertEquals("Нужно указать вариант: '${RandomWordProvider.Provider.values().contentToString()}'", captor?.value)
    }

    @Test
    fun execute2(@Mock event: CommandEvent) {
        val command: Command = SetRandomWordProvider()
        Mockito.`when`(event.args).thenReturn("INVALID")

        val method = Command::class.java.getDeclaredMethod("execute", CommandEvent::class.java)
        method.isAccessible = true
        method.invoke(command, event)

        Mockito.verify(event).reply(captor?.capture())
        Assertions.assertEquals("Нужно указать вариант: '${RandomWordProvider.Provider.values().contentToString()}'", captor?.value)
    }

    @Test
    fun execute3(@Mock event: CommandEvent) {
        val command: Command = SetRandomWordProvider()
        Mockito.`when`(event.args).thenReturn("CASTLOTS")

        val method = Command::class.java.getDeclaredMethod("execute", CommandEvent::class.java)
        method.isAccessible = true
        method.invoke(command, event)

        Mockito.verify(event).reply(captor?.capture())
        Assertions.assertEquals("Установлено CASTLOTS", captor?.value)
    }

}
