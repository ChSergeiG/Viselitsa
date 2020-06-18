package ru.chsergeig.bot.viselitsa

import org.apache.commons.lang.StringUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class RandomWordProviderTest {

    @Test
    fun getWord() {
        Assertions.assertTrue(StringUtils.isNotBlank(RandomWordProvider().getWord(RandomWordProvider.Provider.SANSTV)))
    }

}
