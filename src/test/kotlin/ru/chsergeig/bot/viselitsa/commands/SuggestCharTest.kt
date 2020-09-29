package ru.chsergeig.bot.viselitsa.commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.User
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import ru.chsergeig.bot.viselitsa.Game
import ru.chsergeig.bot.viselitsa.UtilsForTests
import ru.chsergeig.bot.viselitsa.resources.PropertyReader

@ExtendWith(MockitoExtension::class)
internal class SuggestCharTest {

    @Captor
    var captor: ArgumentCaptor<String>? = null

    @AfterEach
    fun afterEach() {
        Game.currentGame = null
    }

    @Test
    fun constructCommand() {
        val command: Command = SuggestChar()
        UtilsForTests.testCommand(
                command,
                PropertyReader.COMMAND.getByKey("suggest.name"),
                arrayListOf(PropertyReader.COMMAND.getByKey("suggest.alias.1")),
                PropertyReader.COMMAND.getByKey("suggest.help"),
                arrayOf(Permission.MESSAGE_WRITE),
                1,
                isGuildOnly = true,
                isHidden = false)
    }

    @Test
    fun execute1(@Mock event: CommandEvent) {
        val command: Command = SuggestChar()

        val method = Command::class.java.getDeclaredMethod("execute", CommandEvent::class.java)
        method.isAccessible = true
        method.invoke(command, event)

        Mockito.verify(event).reply(captor?.capture())
        Assertions.assertEquals(PropertyReader.COMMAND.getByKey("suggest.noGame"), captor?.value)
    }

    @Test
    @Disabled
    fun execute2(@Mock event: CommandEvent, @Mock user: User) {
        val command: Command = SuggestChar()
        Game.currentGame = Game("ТЕСТ")
        Mockito.`when`(event.args).thenReturn("Е")
        Mockito.`when`(event.author).thenReturn(user)
        Mockito.`when`(user.id).thenReturn("mock")

        val method = Command::class.java.getDeclaredMethod("execute", CommandEvent::class.java)
        method.isAccessible = true
        method.invoke(command, event)

        Mockito.verify(event).reply(captor?.capture())
        Assertions.assertTrue(captor?.value!!.contains("Есть такая буква (Е)"))
    }

    @Test
    fun execute3(@Mock event: CommandEvent) {
        val command: Command = SuggestChar()
        Game.currentGame = Game("ТЕСТ")
        Game.currentGame?.terminate()

        val method = Command::class.java.getDeclaredMethod("execute", CommandEvent::class.java)
        method.isAccessible = true
        method.invoke(command, event)

        Mockito.verify(event).reply(captor?.capture())
        Assertions.assertEquals(PropertyReader.COMMAND.getByKey("suggest.gameFinished"), captor?.value)
    }

}
