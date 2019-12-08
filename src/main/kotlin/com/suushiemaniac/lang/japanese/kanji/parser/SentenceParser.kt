package com.suushiemaniac.lang.japanese.kanji.parser

import com.suushiemaniac.lang.japanese.kanji.util.isHiragana
import com.suushiemaniac.lang.japanese.kanji.util.isKatakana

abstract class NewlineGroupParser<T>(rawContent: String): FileParser<T>(rawContent) {
    override fun getAssociations(): Map<String, T> {
        val paragraphs = rawContent.split("\r\n\r\n", "\n\n", "\r\r")

        val symbolGroups = paragraphs.associate {
            val lines = it.lines().filter(String::isNotBlank)

            lines.first() to lines.drop(1)
        }

        return symbolGroups.mapValues { getValues(it.value) }
    }

    abstract fun getValues(assocLines: List<String>): T
}

class OnYomiParser(rawContent: String): NewlineGroupParser<List<String>>(rawContent) {
    override fun getValues(assocLines: List<String>) = assocLines.filter { it.isKatakana }
}

class KunYomiParser(rawContent: String): NewlineGroupParser<List<String>>(rawContent) {
    override fun getValues(assocLines: List<String>) = assocLines.filter { it.isHiragana }
}

data class SampleWord(val kanji: String, val reading: String, val translation: String, val sentence: String)

class SampleWordParser(rawContent: String): NewlineGroupParser<List<SampleWord>>(rawContent) {
    override fun getValues(assocLines: List<String>): List<SampleWord> {
        return assocLines.map {
            val (one, two, three, four) = it.split("\t")
            SampleWord(one, two, three, four)
        }
    }
}