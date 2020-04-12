package com.suushiemaniac.lang.japanese.kanji.util

import com.suushiemaniac.lang.japanese.kanji.model.Kanji
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.*
import com.suushiemaniac.lang.japanese.kanji.model.vocabulary.VocabTagModifier
import com.suushiemaniac.lang.japanese.kanji.source.KanjiSource

fun String.alignReadingsWith(readingsRaw: String, kanjiSource: KanjiSource): List<AlignedReadingToken> {
    val pluckedKanji = this.pluckKanji()

    return zipReadingsExact(pluckedKanji, readingsRaw, kanjiSource)
}

fun String.pluckKanji(): List<String> {
    val pluckedGroups = this.pluckKanjiGroups()

    return pluckedGroups.flatMap {
        if (it.first().toString().isProbablyKanji()) it.toStringifiedChars() else it.singletonList()
    }
}

tailrec fun String.pluckKanjiGroups(accu: List<String> = emptyList()): List<String> {
    if (this.isEmpty()) {
        return accu
    }

    val kanjiGroup = this.takeWhile { it.toString().isProbablyKanji() }

    if (kanjiGroup.isNotEmpty()) {
        return this.drop(kanjiGroup.length).pluckKanjiGroups(accu + kanjiGroup)
    }

    val nonKanji = this.takeWhile { !it.toString().isProbablyKanji() }
    return this.drop(nonKanji.length).pluckKanjiGroups(accu + nonKanji)
}

private fun zipReadingsExact(
    segments: List<String>,
    rawTemplate: String,
    kanjiSource: KanjiSource,
    accu: List<AlignedReadingToken> = emptyList()
): List<AlignedReadingToken> {
    if (segments.isEmpty()) {
        return if (rawTemplate.isEmpty()) accu else emptyList()
    }

    val next = segments.first()
    val nextIsKanji = next.length == 1 && next.isProbablyKanji()

    if (nextIsKanji) {
        val readingVariations = kanjiSource.lookupSymbol(next.first())?.allReadings()?.let { rs ->
            rs.associateWith { it.possibleAlternateKatakanaReadings() - (rs - it) }.invertMultiMap()
        }.orEmpty()

        val adequateReadings =
            readingVariations.keys.filter {
                rawTemplate.startsWith(it.toKatakana()) ||
                        rawTemplate.startsWith(it.toHiragana()) ||
                        rawTemplate.startsWith(it)
            }

        val subsequentMatches = adequateReadings.asSequence().map {
            val readingFromTemplate = rawTemplate.take(it.length)

            val baseReading = readingVariations[readingFromTemplate]?.takeUnless { _ -> it == readingFromTemplate }
            val readingModel = KanjiToken(next.first(), readingFromTemplate, baseReading)

            zipReadingsExact(
                segments.drop(1),
                rawTemplate.drop(readingModel.reading.length),
                kanjiSource,
                accu + readingModel
            )
        }

        return subsequentMatches.find { it.isNotEmpty() }.orEmpty()
    } else {
        val readingFromTemplate = rawTemplate.take(next.length)
        val readingModel = KanaToken(readingFromTemplate)

        return zipReadingsExact(
            segments.drop(1),
            rawTemplate.drop(readingModel.reading.length),
            kanjiSource,
            accu + readingModel
        )
    }
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