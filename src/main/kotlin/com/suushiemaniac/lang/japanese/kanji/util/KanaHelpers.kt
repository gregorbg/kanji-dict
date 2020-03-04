package com.suushiemaniac.lang.japanese.kanji.util

import com.mariten.kanatools.KanaConverter

fun String.containsOnlyKatakana() = this == this.toKatakana()
fun String.containsOnlyHiragana() = this == this.toHiragana()

fun String.toKatakana() = KanaConverter.convertKana(this, KanaConverter.OP_ZEN_HIRA_TO_ZEN_KATA)
fun String.toHiragana() = KanaConverter.convertKana(this, KanaConverter.OP_ZEN_KATA_TO_ZEN_HIRA)

private fun String.toHankakuKatakana() =
    KanaConverter.convertKana(this.toKatakana(), KanaConverter.OP_ZEN_KATA_TO_HAN_KATA)

private fun String.toZenkakuKatakana() =
    KanaConverter.convertKana(this.toKatakana(), KanaConverter.OP_HAN_KATA_TO_ZEN_KATA)

fun Char.katakanaDiacriticVariants(): List<Char> {
    val hankakuSymbol = this.toString().toKatakana().toHankakuKatakana()

    val possibleCognates = DIACRITIC_MARKS.map { "$hankakuSymbol$it" }
        .map { KanaConverter.convertKana(it, KanaConverter.OP_HAN_KATA_TO_ZEN_KATA) }

    return possibleCognates.filter { it.length == 1 }
        .map { it.toZenkakuKatakana() }
        .map { it.first() }
}

val DIACRITIC_MARKS = listOf(KanaConverter.HANKAKU_ASPIRATED_MARK, KanaConverter.HANKAKU_VOICED_MARK)
const val GLOTTAL_STOP_ZEN_KATAKANA = 'ッ'

fun String.possibleAlternateKatakanaReadings(): List<String> {
    val prefixes = this.first().katakanaDiacriticVariants()
        .map { it + this.drop(1).toKatakana() }

    val suffixCandidates = prefixes + this
    val suffixed = suffixCandidates.map { it.dropLast(1).toKatakana() + GLOTTAL_STOP_ZEN_KATAKANA }

    return prefixes + suffixed
}
