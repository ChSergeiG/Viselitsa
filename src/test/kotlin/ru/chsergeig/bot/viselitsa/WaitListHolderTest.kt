package ru.chsergeig.bot.viselitsa

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class WaitListHolderTest {

    @Test
    fun checkTimeSets() {
        WaitListHolder.setTimeToWait(1_000_000_000)
        Assertions.assertEquals(1_000_000_000L, WaitListHolder.getTimeToWait())
    }

    @Test
    fun checkFillingAndRotCorrectly() {
        WaitListHolder.setTimeToWait(1_000_000_000)
        WaitListHolder.add("id1")
        WaitListHolder.add("id2")
        WaitListHolder.add("id3")
        Assertions.assertTrue(WaitListHolder.checkWaits("id1"))
        Assertions.assertTrue(WaitListHolder.checkWaits("id2"))
        Assertions.assertTrue(WaitListHolder.checkWaits("id3"))
        Thread.sleep(1200L)
        Assertions.assertFalse(WaitListHolder.checkWaits("id1"))
        Assertions.assertFalse(WaitListHolder.checkWaits("id2"))
        Assertions.assertFalse(WaitListHolder.checkWaits("id3"))
    }

    @Test
    fun checkEnableDisable() {
        Assertions.assertTrue(WaitListHolder.enabled())
        WaitListHolder.disable()
        Assertions.assertFalse(WaitListHolder.enabled())
        WaitListHolder.enable()
        Assertions.assertTrue(WaitListHolder.enabled())

    }

    @Test
    fun checkFlushCorrectly1() {
        WaitListHolder.setTimeToWait(1_000_000_000L)
        WaitListHolder.add("id1")
        WaitListHolder.add("id2")
        WaitListHolder.add("id3")
        Assertions.assertTrue(WaitListHolder.checkWaits("id1"))
        Assertions.assertTrue(WaitListHolder.checkWaits("id2"))
        Assertions.assertTrue(WaitListHolder.checkWaits("id3"))
        WaitListHolder.flush()
        Assertions.assertFalse(WaitListHolder.checkWaits("id1"))
        Assertions.assertFalse(WaitListHolder.checkWaits("id2"))
        Assertions.assertFalse(WaitListHolder.checkWaits("id3"))
    }

    @Test
    fun checkFlushCorrectly2() {
        WaitListHolder.setTimeToWait(1_000_000_000L)
        WaitListHolder.add("id1")
        WaitListHolder.add("id2")
        WaitListHolder.add("id3")
        Assertions.assertTrue(WaitListHolder.checkWaits("id1"))
        Assertions.assertTrue(WaitListHolder.checkWaits("id2"))
        Assertions.assertTrue(WaitListHolder.checkWaits("id3"))
        WaitListHolder.disable()
        Assertions.assertFalse(WaitListHolder.checkWaits("id1"))
        Assertions.assertFalse(WaitListHolder.checkWaits("id2"))
        Assertions.assertFalse(WaitListHolder.checkWaits("id3"))
        WaitListHolder.enable()
        Assertions.assertFalse(WaitListHolder.checkWaits("id1"))
        Assertions.assertFalse(WaitListHolder.checkWaits("id2"))
        Assertions.assertFalse(WaitListHolder.checkWaits("id3"))
    }

}
