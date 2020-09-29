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
internal class RandomWordTest {

    @Captor
    var captor: ArgumentCaptor<String>? = null

    @AfterEach
    fun afterEach() {
        Game.currentGame = null
    }

    @Test
    fun constructCommand() {
        val command: Command = RandomWord()
        UtilsForTests.testCommand(
                command,
                COMMAND.getByKey("random.name"),
                arrayListOf(),
                COMMAND.getByKey("random.help"),
                arrayOf(Permission.MESSAGE_EMBED_LINKS),
                1,
                isGuildOnly = false,
                isHidden = false)
    }

    @Test
    fun execute(@Mock event: CommandEvent) {
        val command: Command = RandomWord()
        Game.currentGame = Game("Test")

        val method = Command::class.java.getDeclaredMethod("execute", CommandEvent::class.java)
        method.isAccessible = true
        method.invoke(command, event)

        Mockito.verify(event).replyError(captor?.capture())
        Assertions.assertEquals(COMMAND.getByKey("random.gameNotFinished"), captor?.value)
    }

}
