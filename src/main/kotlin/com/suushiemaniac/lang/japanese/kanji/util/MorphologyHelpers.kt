package com.suushiemaniac.lang.japanese.kanji.util

import com.atilika.kuromoji.ipadic.Token
import com.atilika.kuromoji.ipadic.Tokenizer
import com.suushiemaniac.lang.japanese.kanji.anki.model.RubyFuriganaFormatter
import com.suushiemaniac.lang.japanese.kanji.model.Kanji
import com.suushiemaniac.lang.japanese.kanji.model.VocabularyItem
import com.suushiemaniac.lang.japanese.kanji.model.reading.KanaReading
import com.suushiemaniac.lang.japanese.kanji.model.reading.KanjiReading
import com.suushiemaniac.lang.japanese.kanji.model.reading.ReadingWithSurfaceForm
import com.suushiemaniac.lang.japanese.kanji.source.KanjiSource
import com.suushiemaniac.lang.japanese.kanji.source.kanjium.KanjiumDatabaseSource

const val SKIP_TOKEN = "_"
val TOKEN_KEYS = listOf("POS-1", "POS-2", "POS-3", "POS-4", "CONJ-TYPE", "CONJ-FORM", "BASE-FORM", "READ", "PRON")

private val tokenizer = Tokenizer()

fun String.tokenizeJapanese(): List<Token> = tokenizer.tokenize(this)

fun String.alignReadingsWith(readingsRaw: String, kanjiSource: KanjiSource): List<ReadingWithSurfaceForm> {
    val pluckedKanji = this.pluckKanji()

    return transformReadings(pluckedKanji, readingsRaw, kanjiSource)
}

tailrec fun String.pluckKanji(accu: List<String> = emptyList()): List<String> {
    if (this.isEmpty()) {
        return accu
    }

    val candidate = this.first().toString()

    if (candidate.isProbablyKanji()) {
        return this.drop(1).pluckKanji(accu + candidate)
    }

    val nonKanji = this.takeWhile { !it.toString().isProbablyKanji() }
    return this.substring(nonKanji.length).pluckKanji(accu + nonKanji)
}

private fun transformReadings(
    segments: List<String>,
    rawTemplate: String,
    kanjiSource: KanjiSource,
    accu: List<ReadingWithSurfaceForm> = emptyList()
): List<ReadingWithSurfaceForm> {
    if (segments.isEmpty()) {
        return accu.takeUnless { rawTemplate.isNotEmpty() }.orEmpty()
    }

    val next = segments.first()
    val nextIsKanji = next.isProbablyKanji() && next.length == 1

    val testReadings = if (nextIsKanji) {
        kanjiSource.lookupSymbol(next.first())?.allReadings()
            ?.flatMap { it.possibleAlternateKatakanaReadings() }
            ?.distinct().orEmpty()
    } else listOf(next)

    val adequateReadings =
        testReadings.filter {
            rawTemplate.startsWith(it.toKatakana()) ||
                    rawTemplate.startsWith(it.toHiragana()) ||
                    rawTemplate.startsWith(it)
        }

    val subsequentMatches = adequateReadings.map {
        val readingFromTemplate = rawTemplate.take(it.length)

        val readingModel = if (nextIsKanji) {
            KanjiReading(next.first(), readingFromTemplate)
        } else KanaReading(readingFromTemplate)

        transformReadings(
            segments.drop(1),
            rawTemplate.drop(readingModel.reading.length),
            kanjiSource,
            accu + readingModel
        )
    }

    return subsequentMatches.find { it.isNotEmpty() }.orEmpty()
}

private fun Kanji.allReadings(): List<String> {
    val rawReadings = this.kunYomi.map { it.coreReading } + this.onYomi.map { it.kanaReading }
    return rawReadings.filterNot { it.isEmpty() }
}

fun main() {
    val kanjiSource = KanjiumDatabaseSource("/home/suushie_maniac/sqldocs/kanjium/data/kanjidb.sqlite")

    val fullText = "思えら"
    val readingRaw = "おもえら"

    val result = fullText.alignReadingsWith(readingRaw, kanjiSource)
    println(VocabularyItem(result, emptyList()).asFurigana(RubyFuriganaFormatter))
}

// https://raw.githubusercontent.com/mifunetoshiro/kanjium/master/data/idc_mappingtable.txt
val IDC_GRAPH_MAPPING =
    listOf(
        "⿰",
        "⿱",
        "⿲",
        "⿳",
        "⿴",
        "⿵",
        "⿶",
        "⿷",
        "⿸",
        "⿹",
        "⿺",
        "囗",
        "品",
        "品u",
        "品l",
        "品r",
        "⿱1",
        "⿰4",
        "⿰5",
        "⿰1",
        "⿰2",
        "⿰3"
    )