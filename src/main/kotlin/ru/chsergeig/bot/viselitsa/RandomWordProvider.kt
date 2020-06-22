package ru.chsergeig.bot.viselitsa

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import org.apache.commons.lang.StringEscapeUtils
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.utils.URIBuilder
import org.apache.http.impl.client.HttpClientBuilder
import ru.chsergeig.bot.viselitsa.exception.SansDeserializationException
import ru.chsergeig.bot.viselitsa.model.CastLotsRandomWordModel
import ru.chsergeig.bot.viselitsa.model.SansTvRandomWordModel
import java.io.BufferedReader
import java.io.InputStreamReader

class RandomWordProvider {

    fun getWord(provider: Provider): String {
        return provider.getWord()
    }

    enum class Provider {

        CASTLOTS {
            override fun getWord(): String {
                val url = "https://castlots.org/generator-slov/generate.php"
                val httpClient = HttpClientBuilder.create().build()
                val request = HttpPost(url)
                var result = ""
                request.addHeader("X-Requested-With", "XMLHttpRequest")
                request.addHeader("Accept", "application/json, text/javascript, */*; q=0.01")
                request.addHeader("Accept-Encoding", "gzip, deflate, br")
                request.addHeader("Accept-Language", "en,en-US;q=0.9,ru-RU;q=0.8,ru;q=0.7")
                httpClient.use { client ->
                    val line = BufferedReader(
                            InputStreamReader(
                                    client.execute(request).entity.content)).readLine()
                    val wordModel = ObjectMapper().readValue(line, CastLotsRandomWordModel::class.java)
                    result = StringEscapeUtils.unescapeJava(wordModel.va)
                }
                return result
            }
        },
        SANSTV {
            override fun getWord(): String {
                val url = "https://sanstv.ru/randomWord/strong-2"
                val uriBuilder = URIBuilder(url)
                uriBuilder
                        .setParameter("ajax", "#result")
                        .setParameter("strong", "2")
                        .setParameter("count", "1")
                        .setParameter("word", "")
                val httpClient = HttpClientBuilder.create().build()

                val request = HttpGet(uriBuilder.build())
                request.addHeader("accept-language", "en,en-US;q=0.9,ru-RU;q=0.8,ru;q=0.7")
                var result = ""
                httpClient.use { client ->
                    val line = BufferedReader(
                            InputStreamReader(
                                    client.execute(request).entity.content)).readLine()
                    val mapper = XmlMapper()
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    val model: SansTvRandomWordModel
                    try {
                        model = mapper.readValue(line, SansTvRandomWordModel::class.java)
                    } catch (e: Exception) {
                        throw SansDeserializationException("Cant deserialize '$line'", e)
                    }

                    result = model.words[0].value
                }
                return result
            }
        },
        ;

        abstract fun getWord(): String

    }

}
