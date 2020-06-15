package ru.chsergeig.bot.viselitsa

import org.apache.commons.lang.StringUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.platform.commons.logging.Logger
import org.junit.platform.commons.logging.LoggerFactory

internal class RandomWordProviderTest {

    private val logger: Logger = LoggerFactory.getLogger(RandomWordProviderTest::class.java)

    @Test
    fun getWord() {
        logger.info { "Get word from API test" }
        Assertions.assertTrue(StringUtils.isNotBlank(RandomWordProvider().getWord()))
    }

}
