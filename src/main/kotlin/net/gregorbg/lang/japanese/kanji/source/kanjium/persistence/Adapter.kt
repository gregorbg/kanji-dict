package net.gregorbg.lang.japanese.kanji.source.kanjium.persistence

import net.gregorbg.lang.japanese.kanji.model.kanjium.Elements
import net.gregorbg.lang.japanese.kanji.model.kanjium.KanjiDictEntry
import net.gregorbg.lang.japanese.kanji.model.kanjium.Radical
import net.gregorbg.lang.japanese.kanji.model.kanjium.enumeration.*
import net.gregorbg.lang.japanese.kanji.model.reading.annotation.KunYomiAnnotationMode
import net.gregorbg.lang.japanese.kanji.model.reading.annotation.KanjiKunYomi
import net.gregorbg.lang.japanese.kanji.model.reading.annotation.KanjiOnYomi
import net.gregorbg.lang.japanese.kanji.model.reading.annotation.SinoReadingEra
import net.gregorbg.lang.japanese.kanji.model.vocabulary.VocabTagModifier
import net.gregorbg.lang.japanese.kanji.model.vocabulary.VocabularyItem
import net.gregorbg.lang.japanese.kanji.source.KanjiSource
import net.gregorbg.lang.japanese.kanji.util.alignSymbolsWith
import net.gregorbg.lang.japanese.kanji.util.japaneseSymbolsToASCII
import net.gregorbg.lang.japanese.kanji.util.splitAndTrim
import net.gregorbg.lang.japanese.kanji.util.trimEmptyChars

fun KanjiDictDao.toModel(): KanjiDictEntry {
    val fullOn = this.onyomi?.splitAndTrim(KANJI_DICT_ONYOMI_SEP).orEmpty().parseOn()
    val fullKun = this.kunyomi?.splitAndTrim(KANJI_DICT_KUNYOMI_SEP).orEmpty().parseKun()
    val regOn = this.regOn?.splitAndTrim(KANJI_DICT_ONYOMI_SEP).orEmpty().parseOn(fullOn)
    val regKun = this.regKun?.splitAndTrim(KANJI_DICT_KUNYOMI_SEP).orEmpty().parseKun()

    return KanjiDictEntry(
        this.id.value.first(),
        this.radical.toModel(),
        this.radVar?.toModel(),
        this.phonetic?.firstOrNull(),
        this.idc,
        this.type?.takeUnless { it.isEmpty() }?.let { KanjiType.parse(it) },
        regOn,
        regKun,
        fullOn,
        fullKun,
        this.nanori?.splitAndTrim(KANJI_DICT_NANORI_SEP).orEmpty(),
        this.strokes,
        KanjiMinistryList.parseList(this.grade),
        KanjiMinistryList.parseEducationGrade(this.grade),
        this.jlpt?.toJLPTLevel(),
        this.kanken?.toKankenLevel(),
        // FIXME this.frequency?.toIntOrNull(),
        this.meaning.splitAndTrim(KANJI_DICT_MEANING_SEP),
        this.compactMeaning?.splitAndTrim(KANJI_DICT_MEANING_SEP).orEmpty()
    )
}

fun JukugoDao.toModel(dbSource: KanjiSource): VocabularyItem {
    val alignedReading = this.jukugo.alignSymbolsWith(this.reading, dbSource)
    val modifiers = VocabTagModifier.entries.filter { m -> m.kana in this.meaning }

    return VocabularyItem(alignedReading, modifiers)
}

private fun List<String>.normalizeAllSymbols() = map { it.japaneseSymbolsToASCII() }

private fun List<String>.parseOn(backingReadings: List<KanjiOnYomi> = emptyList()): List<KanjiOnYomi> {
    val helperIndex = backingReadings.associateWith { it.historic }
        .mapKeys { it.key.kanaReading }

    return normalizeAllSymbols().map {
        val fakeParse = KunYomiAnnotationMode.BracketKunYomiParser.parse(it)

        val epoch = fakeParse.okurigana?.first()?.let(SinoReadingEra.Companion::parseSymbol)
        val indexEpoch = epoch ?: helperIndex[fakeParse.coreReading]

        KanjiOnYomi(
            fakeParse.coreReading,
            indexEpoch
        )
    }
}

private fun List<String>.parseKun(): List<KanjiKunYomi> {
    return normalizeAllSymbols().map { KunYomiAnnotationMode.BracketKunYomiParser.parse(it) }
}

private fun String.toJLPTLevel(): Int? {
    if (this.isEmpty())
        return null

    return this[1].toString().toIntOrNull()
}

private fun String.toKankenLevel(): Int? {
    val baseLevel = takeLastWhile { it.isDigit() }.toIntOrNull() ?: return null
    val preAdd = if (startsWith("pre")) 1 else 0

    return when (baseLevel) {
        1 -> baseLevel + preAdd
        2 -> baseLevel + 1 + preAdd
        else -> baseLevel + 2
    }
}

const val KANJI_DICT_ONYOMI_SEP = "、"
const val KANJI_DICT_KUNYOMI_SEP = KANJI_DICT_ONYOMI_SEP
const val KANJI_DICT_NANORI_SEP = KANJI_DICT_ONYOMI_SEP
const val KANJI_DICT_MEANING_SEP = ";"

fun ElementsDao.toModel(): Elements {
    val partOfData = this.partOf?.splitAndTrim(ELEMENTS_PART_OF_SEP)
        ?.flatMap { it.toList() }.orEmpty()

    return Elements(
        this.kanji.id.value.first(),
        this.idc,
        this.elements.split(EMPTY_SEP).trimEmptyChars(),
        this.extraElements?.split(EMPTY_SEP).orEmpty().trimEmptyChars(),
        this.kanjiParts?.splitAndTrim(ELEMENTS_KANJI_PARTS_SEP).orEmpty().trimEmptyChars(),
        partOfData,
        this.compactMeaning
    )
}

const val ELEMENTS_KANJI_PARTS_SEP = ","
const val ELEMENTS_PART_OF_SEP = ELEMENTS_KANJI_PARTS_SEP

fun <T : RadicalBaseDao> T.toModel(): Radical {
    val parentRef = if (this is RadVarDao) this.parentRef.toModel() else null

    return Radical(
        this.id.value.first(),
        parentRef,
        this.number,
        this.strokes,
        this.names.splitAndTrim(RADICAL_NAME_SEP),
        this.meaning.splitAndTrim(RADICAL_MEANING_SEP),
        this.notes
    )
}

const val RADICAL_NAME_SEP = "・"
const val RADICAL_MEANING_SEP = ", "

const val EMPTY_SEP = ""
