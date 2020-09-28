package ru.chsergeig.bot.viselitsa

import com.jagrosh.jdautilities.command.CommandEvent
import org.apache.commons.lang.StringUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.mockito.ArgumentMatchers.isA
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.util.stream.Stream

@ExtendWith(MockitoExtension::class)
internal class UtilsTest {

    private val args: Array<String> = arrayOf("prefix2=abc", "prefix3=", "prefix5bla")

    @Test
    @DisplayName("Empty check-n-get")
    fun checkSingleArgAndGet1(@Mock event: CommandEvent) {
        Mockito.lenient().`when`(event.args).thenReturn(null)
        Mockito.doNothing().`when`(event).replyError(isA(String::class.java))
        try {
            Utils.checkSingleArgAndGet(event, "NOT_SINGLE", "EMPTY")
        } catch (e: Exception) {
            Assertions.assertTrue(StringUtils.equals(
                    e.message,
                    "EMPTY"
            ))
            Assertions.assertTrue(
                    e.javaClass == java.lang.RuntimeException::class.java
            )
        }
        Mockito.lenient().`when`(event.args).thenReturn("")
        Mockito.doNothing().`when`(event).replyError(isA(String::class.java))
        try {
            Utils.checkSingleArgAndGet(event, "NOT_SINGLE", "EMPTY")
        } catch (e: Exception) {
            Assertions.assertTrue(StringUtils.equals(
                    e.message,
                    "EMPTY"
            ))
            Assertions.assertTrue(
                    e.javaClass == java.lang.RuntimeException::class.java
            )
        }
        Mockito.lenient().`when`(event.args).thenReturn("                ")
        Mockito.doNothing().`when`(event).replyError(isA(String::class.java))
        try {
            Utils.checkSingleArgAndGet(event, "NOT_SINGLE", "EMPTY")
        } catch (e: Exception) {
            Assertions.assertTrue(StringUtils.equals(
                    e.message,
                    "EMPTY"
            ))
            Assertions.assertTrue(
                    e.javaClass == java.lang.RuntimeException::class.java
            )
        }
    }

    @Test
    @DisplayName("Valid check-n-get")
    fun checkSingleArgAndGet2(@Mock event: CommandEvent) {
        Mockito.lenient().`when`(event.args).thenReturn("VALUE")
        Assertions.assertTrue(StringUtils.equals(
                Utils.checkSingleArgAndGet(event, "NOT_SINGLE", "EMPTY"),
                "VALUE"
        ))
        Mockito.lenient().`when`(event.args).thenReturn("    VALUE    ")
        Assertions.assertTrue(StringUtils.equals(
                Utils.checkSingleArgAndGet(event, "NOT_SINGLE", "EMPTY"),
                "VALUE"
        ))
    }

    @Test
    @DisplayName("Not valid check-n-get")
    fun checkSingleArgAndGet3(@Mock event: CommandEvent) {
        Mockito.lenient().`when`(event.args).thenReturn("VALUE1 VALUE2")
        Mockito.doNothing().`when`(event).replyError(isA(String::class.java))
        try {
            Utils.checkSingleArgAndGet(event, "NOT_SINGLE", "EMPTY")
        } catch (e: Exception) {
            Assertions.assertTrue(StringUtils.equals(
                    e.message,
                    "NOT_SINGLE"
            ))
            Assertions.assertTrue(
                    e.javaClass == java.lang.RuntimeException::class.java
            )
        }
        Mockito.lenient().`when`(event.args).thenReturn("    VALUE1    VALUE2    ")
        Mockito.doNothing().`when`(event).replyError(isA(String::class.java))
        try {
            Utils.checkSingleArgAndGet(event, "NOT_SINGLE", "EMPTY")
        } catch (e: Exception) {
            Assertions.assertTrue(StringUtils.equals(
                    e.message,
                    "NOT_SINGLE"
            ))
            Assertions.assertTrue(
                    e.javaClass == java.lang.RuntimeException::class.java
            )
        }
    }

    @Test
    @Disabled
    @DisplayName("Environment value")
    fun safeGetValueFromCliTest1() {
        Assertions.assertTrue(StringUtils.equals(
                Utils.safeGetValueFromEvOrCli(Array(0) { null.toString() }, "", "", "USERNAME"),
                System.getenv()["USERNAME"]
        ))
    }

    @Test
    @DisplayName("Common CLI value with prefix")
    fun safeGetValueFromCliTest2() {
        Assertions.assertTrue(StringUtils.equals(
                Utils.safeGetValueFromEvOrCli(args, "prefix2=", "Prefix test 2", ""),
                "abc"
        ))
    }

    @Test
    @DisplayName("Empty CLI value with prefix")
    fun safeGetValueFromCliTest3() {
        Assertions.assertTrue(StringUtils.equals(
                Utils.safeGetValueFromEvOrCli(args, "prefix3=", "Prefix test 3", ""),
                ""
        ))
    }

    @Test
    @DisplayName("CLI value by not defined prefix")
    fun safeGetValueFromCliTest4() {
        val thrown3: RuntimeException = Assertions.assertThrows(RuntimeException::class.java) {
            Utils.safeGetValueFromEvOrCli(args, "prefix4=", "Prefix test 4", "")
        }
        Assertions.assertTrue(StringUtils.equals(
                thrown3.message,
                "No CLI value with prefix prefix4="
        ))

    }

    @Test
    @DisplayName("CLI value by invalid prefix")
    fun safeGetValueFromCliTest5() {
        val thrown4: RuntimeException = Assertions.assertThrows(RuntimeException::class.java) {
            Utils.safeGetValueFromEvOrCli(args, "prefix5", "Prefix test 5", "")
        }
        Assertions.assertTrue(StringUtils.equals(
                thrown4.message,
                "Prefix test 5 is empty or not valid"
        ))
    }

    @ParameterizedTest
    @ArgumentsSource(PurifyProvider::class)
    fun purifyWord(input: String, output: String) {
        val dictionary: MutableMap<String, Boolean> = HashMap()
        dictionary["Aa"] = false
        dictionary["Bb"] = false
        dictionary["Cc"] = false
        dictionary["Dd"] = false

        val purified: String = Utils.purifyWord(input, dictionary)
        Assertions.assertTrue(StringUtils.equals(purified, output))
    }

    class PurifyProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                    Arguments.of("abcd", "abcd"),
                    Arguments.of("AbCd", "AbCd"),
                    Arguments.of("abcdefabcd", "abcdabcd"),
                    Arguments.of("ABCD123", "ABCD"),
                    Arguments.of("ABCD--abcd", "ABCDabcd"),
                    Arguments.of("A B C D", "ABCD"),
                    Arguments.of("A1234567890-=!@#\$%^&*()_+!\"№;%:?\\|/йцукенгшщзхъфывапролджэячсмитьбю.B", "AB")
            )
        }
    }

}
