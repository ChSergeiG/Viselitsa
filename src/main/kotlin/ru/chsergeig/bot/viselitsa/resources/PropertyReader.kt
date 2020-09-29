package ru.chsergeig.bot.viselitsa.resources

import java.io.Reader
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

enum class PropertyReader(private val file: String) {
    COMMAND("command.properties") {
        override fun getByKey(key: String): String {
            return getProps().getProperty(key)
        }
    },
    MESSAGE("messages.properties") {
        override fun getByKey(key: String): String {
            return getProps().getProperty(key)
        }
    },
    ;

    private var props = Properties()

    init {
        val url = PropertyReader::class.java.classLoader.getResource(file)
        val reader: Reader = Files.newBufferedReader(Paths.get(url.toURI()))
        props.load(reader)
    }

    fun getProps(): Properties {
        return props
    }

    abstract fun getByKey(key: String): String
}