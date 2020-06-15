package ru.chsergeig.bot.viselitsa

import org.apache.commons.lang.StringUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.junit.platform.commons.logging.Logger
import org.junit.platform.commons.logging.LoggerFactory
import java.util.stream.Stream

internal class UtilsTest {

    private val logger: Logger = LoggerFactory.getLogger(UtilsTest::class.java)

    @Test
    fun safeGetValueFromCliTest() {
        val args: Array<String> = arrayOf("prefix1=abc", "prefix2=", "prefix4bla")

        logger.info { "Common cli value with prefix" }
        Assertions.assertTrue(StringUtils.equals(
                Utils.safeGetValueFromEvOrCli(args, "prefix1=", "Prefix test 1", ""),
                "abc"
        ))

        logger.info { "Empty cli value with prefix" }
        Assertions.assertTrue(StringUtils.equals(
                Utils.safeGetValueFromEvOrCli(args, "prefix2=", "Prefix test 2", ""),
                ""
        ))

        logger.info { "Cli value by not defined prefix" }
        val thrown3: RuntimeException = Assertions.assertThrows(RuntimeException::class.java) {
            Utils.safeGetValueFromEvOrCli(args, "prefix3=", "Prefix test 3", "")
        }
        Assertions.assertTrue(StringUtils.equals(
                thrown3.message,
                "No CLI value with prefix prefix3="
        ))

        logger.info { "Cli value by invalid prefix" }
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
        logger.info { "Purify '$input'. Got '$purified'" }
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
                    Arguments.of("A1234567890-=!@#$%^&*()_+!\"№;%:?\\|/йцукенгшщзхъфывапролджэячсмитьбю.B", "AB")
            )
        }
    }

}
