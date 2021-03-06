package ru.chsergeig.bot.viselitsa

import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.entities.User
import org.apache.commons.lang.StringUtils
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import ru.chsergeig.bot.viselitsa.resources.PropertyReader.MESSAGE
import java.util.stream.Stream

@ExtendWith(MockitoExtension::class)
internal class GameTest {

    companion object {
        @JvmStatic
        private fun gameLeftTurns(): Stream<Arguments> = Stream.of(
                Arguments.of("", 15),
                Arguments.of("АБ", 14),
                Arguments.of("АБВ", 13),
                Arguments.of("АБВВВВ", 13),
                Arguments.of("АБВГДЕ", 11),
                Arguments.of("АБВГДЕЖЗИЙКЛМНОПР", 5),
                Arguments.of("АБВГДЕЖЗИЙКЛМНОПРСТУФХ", 5)
        )
    }

    @BeforeEach
    fun disableWaitList() {
        WaitListHolder.disable()
    }

    @AfterEach
    fun enableWaitList() {
        WaitListHolder.enable()
    }

    @Test
    fun constructGame() {
        val game = Game("Слово")
        Assertions.assertDoesNotThrow { game.getCurrentStatus() }
    }

    @Test
    @DisplayName("Check getCurrentStatus() return valid information")
    fun getCurrentStatusTest1() {
        val game = Game("Слово")
        val status = game.getCurrentStatus()

        Assertions.assertTrue(StringUtils.contains(status, "Осталось попыток: ${game.leftTurns}"))
        Assertions.assertTrue(StringUtils.contains(status, "Слово: ```_ _ _ _ _```"))
        Assertions.assertTrue(StringUtils.contains(status, "Буквы: А Б В Г "))
    }

    @Test
    fun suggestCharTest1EN(@Mock event: CommandEvent, @Mock user: User) {
        Mockito.`when`(event.author).thenReturn(user)
        Mockito.`when`(user.id).thenReturn("mock_user_id")

        val game = Game("Слово")
        suggestChars(event, game, "C", "K", "J", "D")

        Assertions.assertTrue(game.isFinished)
    }


    @Test
    fun suggestCharTest1RU(@Mock event: CommandEvent, @Mock user: User) {
        Mockito.`when`(event.author).thenReturn(user)
        Mockito.`when`(user.id).thenReturn("mock_user_id")

        val game = Game("Слово")
        suggestChars(event, game, "С", "Л", "О", "В")

        Assertions.assertTrue(game.isFinished)
    }

    @Test
    @DisplayName("Check getCurrentStatus() return valid information after game finished")
    fun getCurrentStatusTest2(@Mock event: CommandEvent, @Mock user: User) {
        Mockito.`when`(event.author).thenReturn(user)
        Mockito.`when`(user.id).thenReturn("mock_user_id")

        val game = Game("Слово")
        suggestChars(event, game, "С", "Л", "О", "В")

        val status = game.getCurrentStatus()
        Assertions.assertTrue(StringUtils.contains(status, "Осталось попыток: ${game.leftTurns}"))
        Assertions.assertTrue(StringUtils.contains(status, "Слово: ```С Л О В О```"))
        Assertions.assertTrue(StringUtils.contains(status, "Буквы: А Б █ Г "))
        Assertions.assertTrue(StringUtils.contains(status, "Подибил <@mock_user_id>"))
        Assertions.assertTrue(StringUtils.contains(status, "<@mock_user_id> отгадал 4 букв за 4 попыток [СЛОВ/]"))
    }

    @Test
    @DisplayName("Check getCurrentStatus() return valid information after game failed")
    fun getCurrentStatusTest3(@Mock event: CommandEvent, @Mock user: User) {
        Mockito.`when`(event.author).thenReturn(user)
        Mockito.`when`(user.id).thenReturn("mock_user_id")

        val game = Game("Слово")
        suggestChars(event, game, "А", "Б", "Г", "Д", "Е", "Ж", "З", "И", "Й", "К", "М", "Н", "П")

        Assertions.assertEquals(0, game.leftTurns)
        Assertions.assertTrue(game.isFinished)
    }

    @ParameterizedTest
    @DisplayName("Check leftTurns initialized correctly")
    @MethodSource("gameLeftTurns")
    fun leftTurnsTest1(word: String, turns: Int) {
        Assertions.assertEquals(turns, Game(word).leftTurns)
    }

    @Test
    @DisplayName("Check leftTurns changes correctly")
    fun leftTurnsTest2(@Mock event: CommandEvent, @Mock user: User) {
        Mockito.`when`(event.author).thenReturn(user)
        Mockito.`when`(user.id).thenReturn("mock_user_id")

        val game = Game("Слово")

        Assertions.assertEquals(13, game.leftTurns)

        Mockito.`when`(event.args).thenReturn("С")
        game.suggestChar(event)
        Assertions.assertEquals(13, game.leftTurns)

        Mockito.`when`(event.args).thenReturn("С")
        game.suggestChar(event)
        Assertions.assertEquals(13, game.leftTurns)

        Mockito.`when`(event.args).thenReturn("Л")
        game.suggestChar(event)
        Assertions.assertEquals(13, game.leftTurns)

        Mockito.`when`(event.args).thenReturn("А")
        game.suggestChar(event)
        Assertions.assertEquals(12, game.leftTurns)

        Mockito.`when`(event.args).thenReturn("А")
        game.suggestChar(event)
        Assertions.assertEquals(12, game.leftTurns)

        Mockito.`when`(event.args).thenReturn("В")
        game.suggestChar(event)
        Assertions.assertEquals(12, game.leftTurns)
    }

    @Test
    fun terminateTest() {
        val game = Game("Слово")
        game.terminate()
        Assertions.assertTrue(StringUtils.contains(game.getCurrentStatus(), MESSAGE.getByKey("bot.game.terminateMessage")))
        Assertions.assertTrue(game.isFinished)
    }

    private fun suggestChars(event: CommandEvent, game: Game, vararg chars: String) {
        for (char in chars) {
            Mockito.`when`(event.args).thenReturn(char)
            game.suggestChar(event)
        }
    }

}
