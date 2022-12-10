package net.gregorbg.lang.japanese.kanji.util

import com.mariten.kanatools.KanaAppraiser
import com.mariten.kanatools.KanaConverter
import net.gregorbg.lang.japanese.kanji.model.reading.annotation.KunYomiAnnotationMode

fun String.containsOnlyKatakana() = this.all { KanaAppraiser.isZenkakuKatakana(it) || it == LENGTHENING_MARK_KUTOTEN }
fun String.containsOnlyHiragana() = this.all(KanaAppraiser::isZenkakuHiragana)
// ASCII and Kutoten are fully distinct!
fun String.containsOnlyAscii() = this.all(KanaAppraiser::isZenkakuAscii)
fun String.containsOnlyKutoten() = this.all(KanaAppraiser::isZenkakuKutoten)
fun String.containsOnlyLetters() = this.all(KanaAppraiser::isZenkakuLetter)
fun String.containsOnlyNumbers() = this.all(KanaAppraiser::isZenkakuNumber)
fun String.containsOnlySymbols() =
    this.containsOnlyAscii() && !this.any(KanaAppraiser::isZenkakuNumber) && !this.any(KanaAppraiser::isZenkakuLetter)

fun String.isHandakuSymbol() = this.toKatakana().all {
    it == HANDAKU_YA || it == HANDAKU_YU || it == HANDAKU_YO
}

fun String.containsOnlyHiraganaOrAnnotations(parser: KunYomiAnnotationMode) =
    this.all { KanaAppraiser.isZenkakuHiragana(it) || it in parser.annotationSymbols }

fun String.isProbablyKanji(considerAlphanumAsKanji: Boolean = false): Boolean {
    return this == REPETITION_MARK_KUTOTEN.toString()
            || this == COUNTER_MARK_KUTOTEN.toString()
            || this.isHeuristicsKanji(considerAlphanumAsKanji)
}

private fun String.isHeuristicsKanji(considerAlphanumAsKanji: Boolean): Boolean {
    val numeralsHack =
        if (considerAlphanumAsKanji) this.toZenkakuAscii().containsOnlySymbols() else this.toZenkakuAscii()
            .containsOnlyAscii()
    return !numeralsHack && !this.containsOnlyHiragana() && !this.containsOnlyKatakana() && !this.containsOnlyKutoten()
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
const val FULLSTOP_KUTOTEN = '。'

const val HANDAKU_YA = 'ャ'
const val HANDAKU_YU = 'ュ'
const val HANDAKU_YO = 'ョ'

fun String.possibleAlternateKatakanaReadings(): List<String> {
    val prefixes = this.first().katakanaDiacriticVariants()
        .map { it + this.drop(1).toKatakana() }

    val suffixCandidates = prefixes + this
    val suffixed = suffixCandidates.map { it.dropLast(1).toKatakana() + GLOTTAL_STOP_ZEN_KATAKANA }

    val computed = prefixes + suffixed + this.toKatakana()
    return computed.distinct().filter { it != GLOTTAL_STOP_ZEN_KATAKANA.toString() }
}
