package com.suushiemaniac.lang.japanese.kanji.util

import com.mariten.kanatools.KanaAppraiser
import com.mariten.kanatools.KanaConverter
import com.suushiemaniac.lang.japanese.kanji.model.reading.annotation.KunYomiAnnotationMode

fun String.containsOnlyKatakana() = this.all { KanaAppraiser.isZenkakuKatakana(it) || it == LENGTHENING_MARK_KUTOTEN }
fun String.containsOnlyHiragana() = this.all(KanaAppraiser::isZenkakuHiragana)
fun String.containsOnlyAscii() = this.all(KanaAppraiser::isZenkakuAscii)
fun String.containsOnlySymbols() = this.containsOnlyAscii() && !this.any(KanaAppraiser::isZenkakuNumber)
fun String.containsOnlyKutoten() = this.all(KanaAppraiser::isZenkakuKutoten)
fun String.containsOnlyAlphanum() = this.containsOnlyAscii() || this.containsOnlyKutoten()
fun String.containsOnlyHiraganaOrAnnotations(parser: KunYomiAnnotationMode) =
    this.all { KanaAppraiser.isZenkakuHiragana(it) || it in parser.annotationSymbols }

fun String.isProbablyKanji(considerNumbersAsKanji: Boolean = false): Boolean {
    return this == REPETITION_MARK_KUTOTEN.toString()
            || this == COUNTER_MARK_KUTOTEN.toString()
            || this.isHeuristicsKanji(considerNumbersAsKanji)
}

private fun String.isHeuristicsKanji(considerNumbersAsKanji: Boolean): Boolean {
    val numeralsHack = if (considerNumbersAsKanji) this.toZenkakuAscii().containsOnlySymbols() else this.toZenkakuAscii().containsOnlyAscii()
    return !numeralsHack && !this.containsOnlyHiragana() && !this.containsOnlyKatakana()
}

fun String.toKatakana() = KanaConverter.convertKana(this, KanaConverter.OP_ZEN_HIRA_TO_ZEN_KATA)
fun String.toHiragana() = KanaConverter.convertKana(this, KanaConverter.OP_ZEN_KATA_TO_ZEN_HIRA)

fun String.toKatakanaNoAccents(): String {
    val conversionOps = KanaConverter.OP_HAN_KATA_TO_ZEN_KATA or KanaConverter.OP_KEEP_DIACRITIC_MARKS_APART

    return KanaConverter.convertKana(this.toHankakuKatakana(), conversionOps)
        .filterNot { it in ZENKAKU_DIACRITIC_MARKS }.toKatakana()
}

private fun String.toHankakuKatakana() =
    KanaConverter.convertKana(this.toKatakana(), KanaConverter.OP_ZEN_KATA_TO_HAN_KATA)

private fun String.toZenkakuKatakana() =
    KanaConverter.convertKana(this.toKatakana(), KanaConverter.OP_HAN_KATA_TO_ZEN_KATA)

private fun String.toZenkakuAscii() =
    KanaConverter.convertKana(this, KanaConverter.OP_HAN_ASCII_TO_ZEN_ASCII)

fun Char.katakanaDiacriticVariants(): List<Char> {
    val hankakuBaseSyllable = this.toString().toKatakanaNoAccents().toHankakuKatakana()

    val possibleCognates = HANKAKU_DIACRITIC_MARKS.map { "$hankakuBaseSyllable$it" }
        .map { it.toZenkakuKatakana() }

    return possibleCognates.filter { it.length == 1 }
        .map { it.toZenkakuKatakana() }
        .map { it.first() }
}

val HANKAKU_DIACRITIC_MARKS = listOf(KanaConverter.HANKAKU_ASPIRATED_MARK, KanaConverter.HANKAKU_VOICED_MARK)
val ZENKAKU_DIACRITIC_MARKS = HANKAKU_DIACRITIC_MARKS.map { it.toString().toZenkakuKatakana().first() }

const val GLOTTAL_STOP_ZEN_KATAKANA = 'ッ'
const val REPETITION_MARK_KUTOTEN = '々'
const val COUNTER_MARK_KUTOTEN = 'ヶ'
const val LENGTHENING_MARK_KUTOTEN = 'ー'

fun String.possibleAlternateKatakanaReadings(): List<String> {
    val prefixes = this.first().katakanaDiacriticVariants()
        .map { it + this.drop(1).toKatakana() }

    val suffixCandidates = prefixes + this
    val suffixed = suffixCandidates.map { it.dropLast(1).toKatakana() + GLOTTAL_STOP_ZEN_KATAKANA }

    val computed = prefixes + suffixed + this.toKatakana()
    return computed.distinct().filter { it != GLOTTAL_STOP_ZEN_KATAKANA.toString() }
}
