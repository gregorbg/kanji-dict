package com.suushiemaniac.lang.japanese.kanji.util

import com.mariten.kanatools.KanaAppraiser
import com.mariten.kanatools.KanaConverter
import com.suushiemaniac.lang.japanese.kanji.model.reading.type.KunYomiAnnotationMode

fun String.containsOnlyKatakana() = this.all(KanaAppraiser::isZenkakuKatakana)
fun String.containsOnlyHiragana() = this.all(KanaAppraiser::isZenkakuHiragana)
fun String.containsOnlyAscii() = this.all(KanaAppraiser::isZenkakuAscii)
fun String.containsOnlyKutoten() = this.all(KanaAppraiser::isZenkakuKutoten)
fun String.containsOnlyAlphanum() = this.containsOnlyAscii() || this.containsOnlyKutoten()
fun String.containsOnlyHiraganaOrAnnotations(parser: KunYomiAnnotationMode) = this.all { KanaAppraiser.isZenkakuHiragana(it) || it in parser.annotationSymbols }

fun String.isProbablyKanji(): Boolean {
    val heuristicsKanji = !this.containsOnlyHiragana() && !this.containsOnlyKatakana() && !this.containsOnlyAlphanum()
    return heuristicsKanji || this == REPETITION_MARK_KUTOTEN.toString()
}

fun String.toKatakana() = KanaConverter.convertKana(this, KanaConverter.OP_ZEN_HIRA_TO_ZEN_KATA)
fun String.toHiragana() = KanaConverter.convertKana(this, KanaConverter.OP_ZEN_KATA_TO_ZEN_HIRA)

fun String.toKatakanaNoAccents() =
    KanaConverter.convertKana(this.toHankakuKatakana(), KanaConverter.OP_HAN_KATA_TO_ZEN_KATA or KanaConverter.OP_KEEP_DIACRITIC_MARKS_APART)
        .filterNot { it in ZENKAKU_DIACRITIC_MARKS }.toKatakana()

private fun String.toHankakuKatakana() =
    KanaConverter.convertKana(this.toKatakana(), KanaConverter.OP_ZEN_KATA_TO_HAN_KATA)

private fun String.toZenkakuKatakana() =
    KanaConverter.convertKana(this.toKatakana(), KanaConverter.OP_HAN_KATA_TO_ZEN_KATA)

fun Char.katakanaDiacriticVariants(): List<Char> {
    val hankakuSymbol = this.toString().toKatakana().toHankakuKatakana()

    val possibleCognates = HANKAKU_DIACRITIC_MARKS.map { "$hankakuSymbol$it" }
        .map { KanaConverter.convertKana(it, KanaConverter.OP_HAN_KATA_TO_ZEN_KATA) }

    return possibleCognates.filter { it.length == 1 }
        .map { it.toZenkakuKatakana() }
        .map { it.first() }
}

val HANKAKU_DIACRITIC_MARKS = listOf(KanaConverter.HANKAKU_ASPIRATED_MARK, KanaConverter.HANKAKU_VOICED_MARK)
val ZENKAKU_DIACRITIC_MARKS = HANKAKU_DIACRITIC_MARKS.map { it.toString().toZenkakuKatakana().first() }

const val GLOTTAL_STOP_ZEN_KATAKANA = 'ッ'
const val REPETITION_MARK_KUTOTEN = '々'

fun String.possibleAlternateKatakanaReadings(): List<String> {
    val prefixes = this.first().katakanaDiacriticVariants()
        .map { it + this.drop(1).toKatakana() }

    val suffixCandidates = prefixes + this
    val suffixed = suffixCandidates.map { it.dropLast(1).toKatakana() + GLOTTAL_STOP_ZEN_KATAKANA }

    val computed = prefixes + suffixed + this.toKatakana()
    return computed.distinct().filter { it != GLOTTAL_STOP_ZEN_KATAKANA.toString() }
}
