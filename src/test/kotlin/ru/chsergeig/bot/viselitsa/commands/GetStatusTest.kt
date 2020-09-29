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
internal class GetStatusTest {

    @Captor
    var captor: ArgumentCaptor<String>? = null

    @AfterEach
    fun afterEach() {
        Game.currentGame = null
    }

    @Test
    fun constructCommand() {
        val command: Command = GetStatus()
        UtilsForTests.testCommand(
                command,
                COMMAND.getByKey("status.name"),
                arrayListOf(),
                COMMAND.getByKey("status.help"),
                arrayOf(Permission.MESSAGE_EMBED_LINKS),
                1,
                isGuildOnly = false,
                isHidden = false)
    }


    @Test
    fun execute1(@Mock event: CommandEvent) {
        val command: Command = GetStatus()
        val method = Command::class.java.getDeclaredMethod("execute", CommandEvent::class.java)
        method.isAccessible = true
        method.invoke(command, event)

        Mockito.verify(event).reply(captor?.capture())
        Assertions.assertEquals(captor?.value, COMMAND.getByKey("status.noGame"))
    }

    @Test
    fun execute2(@Mock event: CommandEvent) {
        val command: Command = GetStatus()
        Game.currentGame = Game("Test")
        Game.currentGame!!.terminate()

        val method = Command::class.java.getDeclaredMethod("execute", CommandEvent::class.java)
        method.isAccessible = true
        method.invoke(command, event)

        Mockito.verify(event).reply(captor?.capture())
        Assertions.assertEquals(captor?.value, COMMAND.getByKey("status.gameFinished"))
    }

    @Test
    fun execute3(@Mock event: CommandEvent) {
        val command: Command = GetStatus()
        Game.currentGame = Game("Test")

        val method = Command::class.java.getDeclaredMethod("execute", CommandEvent::class.java)
        method.isAccessible = true
        method.invoke(command, event)

        Mockito.verify(event).reply(captor?.capture())
        Assertions.assertTrue(captor?.value!!.contains("Слово: ```_ _ _ _```"))
    }

}
