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
import ru.chsergeig.bot.viselitsa.UtilsForTests
import ru.chsergeig.bot.viselitsa.resources.PropertyReader.COMMAND


@ExtendWith(MockitoExtension::class)
internal class AbortGameTest {

    @Captor
    var captor: ArgumentCaptor<String>? = null

    @AfterEach
    fun afterEach () {
        Game.currentGame = null
    }

    @Test
    fun constructCommand() {
        val command: Command = AbortGame()
        UtilsForTests.testCommand(
                command,
                COMMAND.getByKey("abort.name"),
                arrayListOf(),
                COMMAND.getByKey("abort.help"),
                arrayOf(Permission.MESSAGE_EMBED_LINKS),
                1,
                isGuildOnly = false,
                isHidden = false)
    }

    @Test
    fun execute1(@Mock event: CommandEvent) {
        val command: Command = AbortGame()
        val method = Command::class.java.getDeclaredMethod("execute", CommandEvent::class.java)
        method.isAccessible = true
        method.invoke(command, event)

        Mockito.verify(event).reply(captor?.capture())
        Assertions.assertEquals(captor?.value, COMMAND.getByKey("abort.noGame"))
    }

    @Test
    fun execute2(@Mock event: CommandEvent) {
        val command: Command = AbortGame()
        Game.currentGame = Game("Test")
        Game.currentGame!!.terminate()

        val method = Command::class.java.getDeclaredMethod("execute", CommandEvent::class.java)
        method.isAccessible = true
        method.invoke(command, event)

        Mockito.verify(event).reply(captor?.capture())
        Assertions.assertEquals(captor?.value, COMMAND.getByKey("abort.gameFinished"))
    }

    @Test
    fun execute3(@Mock event: CommandEvent) {
        val command: Command = AbortGame()
        Game.currentGame = Game("Test")

        val method = Command::class.java.getDeclaredMethod("execute", CommandEvent::class.java)
        method.isAccessible = true
        method.invoke(command, event)

        Mockito.verify(event).reply(captor?.capture())
        Assertions.assertEquals(captor?.value, COMMAND.getByKey("abort.success"))
    }

}
