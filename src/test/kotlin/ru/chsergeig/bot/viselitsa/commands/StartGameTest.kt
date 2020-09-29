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
internal class StartGameTest {

    @Captor
    var captor: ArgumentCaptor<String>? = null

    @AfterEach
    fun afterEach() {
        Game.currentGame = null
    }

    @Test
    fun constructCommand() {
        val command: Command = StartGame()
        UtilsForTests.testCommand(
                command,
                COMMAND.getByKey("start.name"),
                arrayListOf(),
                COMMAND.getByKey("start.help"),
                arrayOf(Permission.MESSAGE_EMBED_LINKS),
                1,
                isGuildOnly = false,
                isHidden = false)
    }

    @Test
    fun execute1(@Mock event: CommandEvent) {
        val command: Command = StartGame()
        Game.currentGame = Game("Test")

        val method = Command::class.java.getDeclaredMethod("execute", CommandEvent::class.java)
        method.isAccessible = true
        method.invoke(command, event)

        Mockito.verify(event).replyError(captor?.capture())
        Assertions.assertEquals(COMMAND.getByKey("start.gameNotFinished"), captor?.value)
    }

    @Test
    fun execute2(@Mock event: CommandEvent) {
        val command: Command = StartGame()
        Mockito.`when`(event.args).thenReturn("ТЕСТ")

        val method = Command::class.java.getDeclaredMethod("execute", CommandEvent::class.java)
        method.isAccessible = true
        method.invoke(command, event)

        Mockito.verify(event).reply(captor?.capture())
        Assertions.assertEquals(Game.currentGame?.word, "ТЕСТ")
        Assertions.assertTrue(captor?.value!!.contains("Слово ТЕСТ принято"))
        Assertions.assertTrue(captor?.value!!.contains("Длина слова: 4"))
        Assertions.assertTrue(captor?.value!!.contains(""))
    }

}
