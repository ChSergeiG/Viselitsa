package ru.chsergeig.bot.viselitsa

import org.apache.commons.lang.StringUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class UtilsTest {

    private val args: Array<String> = arrayOf("prefix1=abc", "prefix2=", "prefix4bla")

    @Test
    @DisplayName("Common CLI value with prefix")
    fun safeGetValueFromCliTest1() {
        Assertions.assertTrue(StringUtils.equals(
                Utils.safeGetValueFromEvOrCli(args, "prefix1=", "Prefix test 1", ""),
                "abc"
        ))
    }

    @Test
    @DisplayName("Empty CLI value with prefix")
    fun safeGetValueFromCliTest2() {
        Assertions.assertTrue(StringUtils.equals(
                Utils.safeGetValueFromEvOrCli(args, "prefix2=", "Prefix test 2", ""),
                ""
        ))
    }

    @Test
    @DisplayName("CLI value by not defined prefix")
    fun safeGetValueFromCliTest3() {
        val thrown3: RuntimeException = Assertions.assertThrows(RuntimeException::class.java) {
            Utils.safeGetValueFromEvOrCli(args, "prefix3=", "Prefix test 3", "")
        }
        Assertions.assertTrue(StringUtils.equals(
                thrown3.message,
                "No CLI value with prefix prefix3="
        ))

    }

    @Test
    @DisplayName("CLI value by invalid prefix")
    fun safeGetValueFromCliTest4() {
        val thrown4: RuntimeException = Assertions.assertThrows(RuntimeException::class.java) {
            Utils.safeGetValueFromEvOrCli(args, "prefix4", "Prefix test 4", "")
        }
        Assertions.assertTrue(StringUtils.equals(
                thrown4.message,
                "Prefix test 4 is empty or not valid"
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
