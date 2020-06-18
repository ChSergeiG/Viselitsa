package ru.chsergeig.bot.viselitsa

import io.github.artsok.RepeatedIfExceptionsTest
import org.apache.commons.lang.StringUtils
import org.junit.jupiter.api.Assertions

internal class RandomWordProviderTest {

    @RepeatedIfExceptionsTest(repeats = 2)
    fun getWord() {
        Assertions.assertTrue(StringUtils.isNotBlank(RandomWordProvider().getWord(RandomWordProviderHolder.provider)))
    }

}
