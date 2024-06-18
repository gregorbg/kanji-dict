package net.gregorbg.lang.japanese.kanji.util

import net.gregorbg.lang.japanese.kanji.model.Kanji
import net.gregorbg.lang.japanese.kanji.model.reading.token.*
import net.gregorbg.lang.japanese.kanji.model.reading.token.level.SymbolLevelToken
import net.gregorbg.lang.japanese.kanji.model.reading.token.level.WordLevelToken
import net.gregorbg.lang.japanese.kanji.model.vocabulary.VocabTagModifier
import net.gregorbg.lang.japanese.kanji.source.KanjiSource

fun String.alignSymbolsWith(readingsRaw: String, kanjiSource: KanjiSource): List<SymbolLevelToken> {
    val pluckedKanji = this.pluckKanji()
    return zipReadingsExact(pluckedKanji, readingsRaw, kanjiSource)
}

fun String.alignSymbolsWith(readingsRaw: String): List<WordLevelToken> {
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
    accu: List<SymbolLevelToken> = emptyList()
): List<SymbolLevelToken> {
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

                    val baseReading = possibleReading.takeUnless { it.toKatakana() == readingFromTemplate.toKatakana() }
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
        val readingModel = KanaToken.fromWord(next)

        return zipReadingsExact(
            segments.drop(1),
            rawTemplate.drop(readingModel.size),
            kanjiSource,
            accu + readingModel
        )
    }
}

@JvmInline
private value class WordLevelAdapter(val symbolLevelToken: SymbolLevelToken) : WordLevelToken, TokenWithSurfaceForm by symbolLevelToken

private fun zipReadings(
    segments: List<String>,
    rawTemplate: String,
    accu: List<WordLevelToken> = emptyList()
): List<WordLevelToken> {
    if (segments.isEmpty()) {
        return if (rawTemplate.isEmpty()) accu else emptyList()
    }

    val next = segments.first()
    val remaining = segments.drop(1)

    fun makeKanjiToken(kanji: String, reading: String) =
        if (kanji.length == 1) WordLevelAdapter(KanjiToken(kanji.first(), reading)) else CompoundKanjiToken(kanji, reading)

    if (next.isProbablyKanji(true)) {
        if (remaining.isEmpty()) {
            val readingModel = if (next.all(Char::isDigit)) CompoundKanaToken(next) else makeKanjiToken(next, rawTemplate)

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
                val readingModel = makeKanjiToken(next, readingFromTemplate)

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
        val readingModel = CompoundKanaToken(next)

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

fun <T : TokenWithSurfaceForm> List<T>.guessVocabModifiers()
        = this.guessVocabModifiersAndReAlign().second

fun <T : TokenWithSurfaceForm> List<T>.guessVocabModifiersAndReAlign(): Pair<List<T>, List<VocabTagModifier>> {
    if (this.size >= 2) {
        val (secondToLast, last) = this.takeLast(2)

        if (last is CompoundKanaToken && secondToLast is KanjiToken) {
            return VocabTagModifier.fromKana(last.kana)?.let {
                this.dropLast(1) to it.singletonList()
            } ?: (this to emptyList())
        }
    }

    return this to emptyList()
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