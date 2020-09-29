package ru.chsergeig.bot.viselitsa

import org.apache.commons.lang.StringUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

internal class RandomWordProviderTest {

    @ParameterizedTest
    @EnumSource(RandomWordProvider.Provider::class)
    fun providerGetWord(provider: RandomWordProvider.Provider) {
        Assertions.assertTrue(StringUtils.isNotBlank(
                provider.getWord()
        ))
    }

    @Test
    fun defaultValue1() {
        Assertions.assertTrue(RandomWordProviderHolder.provider == RandomWordProvider.Provider.CASTLOTS)
    }

    @Test
    fun defaultValue2() {
        Assertions.assertTrue(StringUtils.isNotBlank(
                RandomWordProvider().getWord(RandomWordProviderHolder.provider)
        ))
    }

}
