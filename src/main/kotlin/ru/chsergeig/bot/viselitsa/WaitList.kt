package ru.chsergeig.bot.viselitsa

import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

class WaitList {

    companion object {

        private val TIME_TO_WAIT_DEFAULT: Long = TimeUnit.SECONDS.toNanos(5L)
        private var timeToWait: Long = TIME_TO_WAIT_DEFAULT

        private var enabled: AtomicBoolean = AtomicBoolean(true)
        private val waitList: MutableMap<Long, String> = HashMap()
        private val poolExecutor = ScheduledThreadPoolExecutor(1)

        private val waitListChecker: Runnable = Runnable {
            waitList.keys.filter { System.nanoTime() - it > timeToWait }.forEach { waitList.remove(it) }

        }

        fun disable() {
            enabled.set(false)
        }

        fun enable() {
            enabled.set(true)
        }

        fun setTimeToWait(timeToWait: Long) {
            this.timeToWait = timeToWait
        }

        fun getTimeToWait(): Long {
            return timeToWait
        }

        fun add(id: String) {
            waitList[System.nanoTime()] = id
        }

        fun checkWaits(id: String): Boolean {
            if (enabled.get()) {
                return waitList.values.contains(id)
            }
            return false
        }

        fun flush() {
            if (poolExecutor.isTerminated || poolExecutor.isTerminating) {
                poolExecutor.shutdownNow()
            }
            waitList.clear()
            poolExecutor.scheduleAtFixedRate(waitListChecker, 0, 100, TimeUnit.MILLISECONDS)
        }

    }

}
