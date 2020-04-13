package com.suushiemaniac.lang.japanese.kanji.util

import com.suushiemaniac.lang.japanese.kanji.model.Kanji
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.*
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.compose.CompositeWordLevelTokens
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.compose.CompositeReadingTokens
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.compose.CompositeTokens
import com.suushiemaniac.lang.japanese.kanji.model.vocabulary.VocabTagModifier
import com.suushiemaniac.lang.japanese.kanji.source.KanjiSource

fun String.alignReadingsWith(readingsRaw: String, kanjiSource: KanjiSource): List<AlignedReadingToken> {
    val pluckedKanji = this.pluckKanji()
    return zipReadingsExact(pluckedKanji, readingsRaw, kanjiSource)
}

fun String.alignReadingsWith(readingsRaw: String): List<ReadingToken> {
    val pluckedKanjiGroups = this.pluckKanjiGroups(true)
    return zipReadings(pluckedKanjiGroups, readingsRaw)
}

fun String.pluckKanji(): List<String> {
    val pluckedGroups = this.pluckKanjiGroups(false)

    return pluckedGroups.flatMap {
        if (it.first().toString().isProbablyKanji(false)) it.toStringifiedChars() else it.singletonList()
    }
}

private tailrec fun String.pluckKanjiGroups(
    considerAlphanumAsKanji: Boolean,
    accu: List<String> = emptyList()
): List<String> {
    if (this.isEmpty()) {
        return accu
    }

    val kanjiGroup = this.takeWhile { it.toString().isProbablyKanji(considerAlphanumAsKanji) }

    if (kanjiGroup.isNotEmpty()) {
        return this.drop(kanjiGroup.length).pluckKanjiGroups(considerAlphanumAsKanji, accu + kanjiGroup)
    }

    val nonKanji = this.takeWhile { !it.toString().isProbablyKanji(considerAlphanumAsKanji) }
    return this.drop(nonKanji.length).pluckKanjiGroups(considerAlphanumAsKanji, accu + nonKanji)
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
    val nextIsKanji = next.length == 1 && next.isProbablyKanji(false)

    if (nextIsKanji) {
        val possibleReadings = kanjiSource.lookupSymbol(next.first())?.allReadings()
            ?: return emptyList()

        for (possibleReading in possibleReadings) {
            val theoreticalVariations = possibleReading.possibleAlternateKatakanaReadings()
            val readingVariations = theoreticalVariations - (possibleReadings - possibleReading)

            for (readingVariation in readingVariations) {
                if (rawTemplate.startsWith(readingVariation)
                    || rawTemplate.startsWith(readingVariation.toHiragana())
                    || rawTemplate.startsWith(readingVariation.toKatakana())
                ) {
                    val readingFromTemplate = rawTemplate.take(readingVariation.length)

                    val baseReading = possibleReading.takeUnless { readingVariation == readingFromTemplate }
                    val readingModel = KanjiToken(next.first(), readingFromTemplate, baseReading)

                    val continuedAlignment = zipReadingsExact(
                        segments.drop(1),
                        rawTemplate.drop(readingModel.reading.length),
                        kanjiSource,
                        accu + readingModel
                    )

                    if (continuedAlignment.isNotEmpty()) {
                        return continuedAlignment
                    }
                }
            }
        }

        return emptyList()
    } else {
        val readingModel = KanaToken(next)

        return zipReadingsExact(
            segments.drop(1),
            rawTemplate.drop(readingModel.reading.length),
            kanjiSource,
            accu + readingModel
        )
    }
}

private fun zipReadings(
    segments: List<String>,
    rawTemplate: String,
    accu: List<ReadingToken> = emptyList()
): List<ReadingToken> {
    if (segments.isEmpty()) {
        return if (rawTemplate.isEmpty()) accu else emptyList()
    }

    val next = segments.first()
    val remaining = segments.drop(1)

    if (next.isProbablyKanji(true)) {
        if (remaining.isEmpty()) {
            val readingModel = if (next.all(Char::isDigit)) KanaToken(next) else CompoundKanjiToken(next, rawTemplate)

            return zipReadings(
                remaining,
                EMPTY_STRING,
                accu + readingModel
            )
        } else {
            val afterNext = remaining.first()
            val afterNextKanaStart = afterNext.first()

            val kanaOccurrences = rawTemplate.mapIndexed { i, c -> i to c }
                .filter { it.second == afterNextKanaStart }
                .map { it.first }

            for (occurrence in kanaOccurrences) {
                val readingFromTemplate = rawTemplate.take(occurrence)
                val readingModel = CompoundKanjiToken(next, readingFromTemplate)

                val continuedAlignment = zipReadings(
                    remaining,
                    rawTemplate.drop(readingModel.reading.length),
                    accu + readingModel
                )

                if (continuedAlignment.isNotEmpty()) {
                    return continuedAlignment
                }
            }

            return emptyList()
        }
    } else {
        val readingModel = KanaToken(next)

        return zipReadings(
            remaining,
            rawTemplate.drop(readingModel.reading.length),
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

fun TokenWithSurfaceForm.unwrap() =
    if (this is CompositeTokens<*>) this.tokens else this.singletonList()

fun ReadingToken.unwrap() =
    if (this is CompositeReadingTokens<*>) this.tokens else this.singletonList()

fun WordLevelToken.unwrap() =
    if (this is CompositeWordLevelTokens<*>) this.tokens else this.singletonList()

fun TokenWithSurfaceForm.flatten(): List<TokenWithSurfaceForm> =
    if (this is CompositeTokens<*>) this.tokens.flatMap { it.flatten() } else this.singletonList()

fun ReadingToken.flatten(): List<ReadingToken> =
    if (this is CompositeReadingTokens<*>) this.tokens.flatMap { it.flatten() } else this.singletonList()

fun WordLevelToken.flatten(): List<WordLevelToken> =
    if (this is CompositeWordLevelTokens<*>) this.tokens.flatMap { it.flatten() } else this.singletonList()

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