package com.suushiemaniac.lang.japanese.kanji.parser

import com.suushiemaniac.lang.japanese.kanji.model.VocabularyItem
import com.suushiemaniac.lang.japanese.kanji.model.kanjium.enumeration.KunYomi
import com.suushiemaniac.lang.japanese.kanji.model.kanjium.enumeration.OnYomi
import com.suushiemaniac.lang.japanese.kanji.util.containsOnlyHiragana
import com.suushiemaniac.lang.japanese.kanji.util.containsOnlyKatakana

abstract class NewlineGroupParser<T>(rawContent: String) : FileParser<T>(rawContent) {
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

class OnYomiParser(rawContent: String) : NewlineGroupParser<List<OnYomi>>(rawContent) {
    override fun getValues(assocLines: List<String>) =
        assocLines.filter { it.containsOnlyKatakana() }
            .map { OnYomi(it) }
}

class KunYomiParser(
    rawContent: String,
    val annotationMode: KunYomiAnnotationMode = KunYomiAnnotationMode.BracketKunYomiParser
) : NewlineGroupParser<List<KunYomi>>(rawContent) {
    override fun getValues(assocLines: List<String>) =
        assocLines.filter { it.containsOnlyHiragana() }
            .map(annotationMode::parse)
}

class VocabularyParser(rawContent: String) : NewlineGroupParser<List<VocabularyItem>>(rawContent) {
    override fun getValues(assocLines: List<String>): List<VocabularyItem> {
        return assocLines.map {
            val parts = it.split("\t")
            val (fullText, reading, transRaw) = parts.take(3)

            val translations = transRaw.split(",").map(String::trim)
            val optSample = parts.getOrNull(4)

            VocabularyItem(fullText, reading, translations, optSample)
        }
    }
}