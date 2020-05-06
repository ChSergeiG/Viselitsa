package ru.chsergeig.bot.viselitsa

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.lang.StringEscapeUtils
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClientBuilder
import ru.chsergeig.bot.viselitsa.model.RandomWordModel
import java.io.BufferedReader
import java.io.InputStreamReader

class RandomWordProvider {

    private val url = "https://castlots.org/generator-slov/generate.php"

    fun getWord(): String {
        val httpClient = HttpClientBuilder.create().build()
        val request = HttpPost(url)
        var result: String? = ""
        request.addHeader("X-Requested-With", "XMLHttpRequest")
        request.addHeader("Accept", "application/json, text/javascript, */*; q=0.01")
        request.addHeader("Accept-Encoding", "gzip, deflate, br")
        request.addHeader("Accept-Language", "en,en-US;q=0.9,ru-RU;q=0.8,ru;q=0.7")
        httpClient.use { client ->
            val line = BufferedReader(
                    InputStreamReader(
                            client.execute(request).entity.content)).readLine()
            val wordModel = ObjectMapper().readValue(line, RandomWordModel::class.java)
            result = StringEscapeUtils.unescapeJava(wordModel.va)
        }
        return result!!
    }

}
