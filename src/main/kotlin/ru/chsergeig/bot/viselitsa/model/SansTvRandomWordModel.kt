package ru.chsergeig.bot.viselitsa.model

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "nt")
class SansTvRandomWordModel {

    @JacksonXmlElementWrapper(localName = "ol")
    var words: MutableList<Word> = ArrayList()

}

class Words {

    @JacksonXmlProperty(localName = "li")
    var words: MutableList<Word> = ArrayList()

}

class Word {

    @JacksonXmlProperty(localName = "a")
    var value: String = ""

}
