package com.suushiemaniac.lang.japanese.kanji.util

import com.atilika.kuromoji.ipadic.Token
import com.atilika.kuromoji.ipadic.Tokenizer
import com.suushiemaniac.lang.japanese.kanji.model.Kanji
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.KanaToken
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.KanjiToken
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.ReadingToken
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.TokenWithSurfaceForm
import com.suushiemaniac.lang.japanese.kanji.model.vocabulary.VocabTagModifier
import com.suushiemaniac.lang.japanese.kanji.source.KanjiSource

const val UNSPECIFIED_SKIP_TOKEN = "*"
val TOKEN_KEYS = listOf("POS-1", "POS-2", "POS-3", "POS-4", "CONJ-TYPE", "CONJ-FORM", "BASE-FORM", "READ", "PRON")

private val tokenizer = Tokenizer()

fun String.tokenizeJapanese(): List<Token> = tokenizer.tokenize(this)

fun String.alignReadingsWith(readingsRaw: String, kanjiSource: KanjiSource): List<ReadingToken> {
    val pluckedKanji = this.pluckKanji()

    return zipReadings(pluckedKanji, readingsRaw, kanjiSource)
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

private fun zipReadings(
    segments: List<String>,
    rawTemplate: String,
    kanjiSource: KanjiSource,
    accu: List<ReadingToken> = emptyList()
): List<ReadingToken> {
    if (segments.isEmpty()) {
        return accu.takeUnless { rawTemplate.isNotEmpty() }.orEmpty()
    }

    val next = segments.first()
    val nextIsKanji = next.isProbablyKanji() && next.length == 1

    val readingVariations = if (nextIsKanji) {
        kanjiSource.lookupSymbol(next.first())?.allReadings()?.let { rs ->
            rs.associateWith { it.possibleAlternateKatakanaReadings() - (rs - it) }
                .invertMultiMap()
        }.orEmpty()
    } else mapOf(next to next)

    val adequateReadings =
        readingVariations.keys.filter {
            rawTemplate.startsWith(it.toKatakana()) ||
                    rawTemplate.startsWith(it.toHiragana()) ||
                    rawTemplate.startsWith(it)
        }

    val subsequentMatches = adequateReadings.map {
        val readingFromTemplate = rawTemplate.take(it.length)

        val readingModel = if (nextIsKanji) {
            val baseReading = readingVariations[readingFromTemplate]?.takeUnless { _ -> it == readingFromTemplate }
            KanjiToken(next.first(), readingFromTemplate, baseReading)
        } else KanaToken(readingFromTemplate)

        zipReadings(
            segments.drop(1),
            rawTemplate.drop(readingModel.reading.length),
            kanjiSource,
            accu + readingModel
        )
    }

    return subsequentMatches.find { it.isNotEmpty() }.orEmpty()
}

private fun Kanji.allReadings(): List<String> {
    val combinedReadings = this.kunYomi + this.onYomi
    return combinedReadings.map { it.standardisedReading }.filterNot { it.isEmpty() }
}

fun List<TokenWithSurfaceForm>.guessVocabModifiers(): List<VocabTagModifier> {
    if (this.size >= 2) {
        val (secondToLast, last) = this.takeLast(2)

        if (last is KanaToken && secondToLast is KanjiToken) {
            return VocabTagModifier.fromKana(last.kana)?.singletonList().orEmpty()
        }
    }

    return emptyList()
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