package com.suushiemaniac.lang.japanese.kanji.anki

import com.suushiemaniac.lang.japanese.kanji.anki.AnkiExporter.JSON
import com.suushiemaniac.lang.japanese.kanji.anki.model.RubyFuriganaFormatter
import com.suushiemaniac.lang.japanese.kanji.model.KanjiElements.Companion.cleanComponents
import com.suushiemaniac.lang.japanese.kanji.model.vocabulary.VocabularyItem
import com.suushiemaniac.lang.japanese.kanji.model.kanjium.KanjiDictEntry
import com.suushiemaniac.lang.japanese.kanji.model.kanjium.Radical
import com.suushiemaniac.lang.japanese.kanji.model.reading.annotation.KanjiKunYomi
import com.suushiemaniac.lang.japanese.kanji.model.reading.annotation.KanjiOnYomi
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.KanjiToken
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.SymbolToken
import com.suushiemaniac.lang.japanese.kanji.model.workbook.WorkbookMetadata
import com.suushiemaniac.lang.japanese.kanji.source.KanjiElementsSource
import com.suushiemaniac.lang.japanese.kanji.source.KanjiSource
import com.suushiemaniac.lang.japanese.kanji.source.TranslationSource
import com.suushiemaniac.lang.japanese.kanji.source.VocabularySource
import com.suushiemaniac.lang.japanese.kanji.util.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer

data class KanjiNote(
    val kanjiSymbol: Char,
    val kunReadingsWithSamples: Map<KanjiKunYomi, List<String>>,
    val onReadingsWithSamples: Map<KanjiOnYomi, List<String>>,
    val rendakuReadingExceptions: Map<VocabularyItem, String>,
    val radicalDescription: String,
    val idcGraphNum: Int,
    val elementsWithName: Map<Char, String>,
    val coreMeaning: String,
    val sampleTranslations: Map<String, String>,
    val sampleReadings: Map<String, SymbolToken>,
    val lesson: Int,
    val id: Int,
    val kanken: String?,
    val jlpt: Int?
) : AnkiDeckNote {
    override fun getCSVFacts(): List<String> {
        val ankiFormatReadings = sampleReadings.mapValues { it.value.asFurigana(RubyFuriganaFormatter) }

        return listOf(
            kanjiSymbol.toString(),
            JSON.encodeToString(
                MapSerializer(String.serializer(), ListSerializer(String.serializer())),
                kunReadingsWithSamples.mapKeys { it.key.coreReading.toHiragana() }),
            JSON.encodeToString(
                MapSerializer(String.serializer(), ListSerializer(String.serializer())),
                onReadingsWithSamples.mapKeys { it.key.kanaReading.toKatakana() }),
            JSON.encodeToString(
                MapSerializer(String.serializer(), String.serializer()),
                rendakuReadingExceptions.mapKeys { it.key.surfaceForm }),
            radicalDescription,
            "<img src=\"idcGraph-$idcGraphNum.png\">",
            elementsWithName.entries.joinToString("<br/>") { "${it.key} ${it.value}" },
            coreMeaning,
            JSON.encodeToString(MapSerializer(String.serializer(), String.serializer()), sampleTranslations),
            JSON.encodeToString(MapSerializer(String.serializer(), String.serializer()), ankiFormatReadings)
        )
    }

    override fun getTags(): List<String> {
        return listOfNotNull("L$lesson", "id-$id", jlpt?.let { "JLPT-N$it" }, kanken?.let { "Kanken:$it" })
    }

    companion object {
        fun from(
            kanji: KanjiDictEntry,
            elementsSource: KanjiElementsSource,
            elementsSymbolSource: KanjiSource,
            elementsTranslationSource: TranslationSource,
            metadata: WorkbookMetadata,
            vocabSource: VocabularySource,
            vocabTranslationSource: TranslationSource
        ): KanjiNote {
            val allSamples = vocabSource.getVocabularyItemsFor(kanji)

            val ankiKunYomi = kanji.regularKunYomi.groupBy { it.standardisedReading }
                .mapValues { it.value.map(KanjiKunYomi::okurigana) }
                .mapKeys { KanjiKunYomi(it.key) }

            val kunModelRawSamples =
                ankiKunYomi.mapValues { allSamples.filterForReadings(it.key.standardisedReading) }
            val onModelSamples =
                kanji.regularOnYomi.associateWith { allSamples.filterForReadings(it.standardisedReading) }

            val usedRawSamples = (kunModelRawSamples.values.flatten() + onModelSamples.values.flatten()).toSet()
            val remainingSamples = allSamples - usedRawSamples

            val kunModelExtendedSamples = ankiKunYomi.mapValuesNotNull {
                remainingSamples.filterForPrefixReadings(it.key.standardisedReading).unlessEmpty()
            }

            val kunModelSamples = kunModelRawSamples.mergeMultiMap(kunModelExtendedSamples)
            val usedSamples = (kunModelSamples.values.flatten() + onModelSamples.values.flatten()).toSet()

            require(allSamples.size == usedSamples.size) { "Missing samples: " + (allSamples - usedSamples).map { it.surfaceForm } }

            val rendakuExceptions = usedSamples.associateWithNotNull {
                it.tokens.firstOrNull { r -> r.surfaceForm == kanji.kanji.toString() }
                    ?.takeIf { r -> r.reading != r.normalizedReading }?.reading
            }

            val radicalDescription =
                kanji.radicalVariant?.let { radicalDesc(it) + " (Variante von " + radicalDesc(kanji.radical) + ")" }
                    ?: radicalDesc(kanji.radical)

            val elements = elementsSource.getElementsFor(kanji)
                ?: error("No valid elements decomposition for $kanji")

            val idcIndex = IDC_GRAPH_MAPPING.indexOf(elements.idc).takeUnless { it == -1 }?.let { it + 1 } ?: 0

            val elementsWithName = elements.cleanComponents(elementsSymbolSource, elementsSource)
                .associateWithNotNull {
                    val elementsSymbol = elementsSymbolSource.lookupSymbol(it)?.let { c ->
                        KanjiToken(c.kanji, c.kanji.toString()) // FIXME hack
                    }

                    val registeredTranslation = elementsSymbol?.let(elementsTranslationSource::getTranslationFor)
                    registeredTranslation?.mainTranslation.orEmpty()
                }

            val suitableMeanings = kanji.compactMeaning.joinToString()

            val translatedSamples = allSamples
                .associateWith { vocabTranslationSource.getTranslationFor(it)?.mainTranslation.orEmpty() }
                .filterKeys { it in usedSamples }

            val readingsForSamples = allSamples.associateBy { it.surfaceForm }
                .filterValues { it in usedSamples }

            return KanjiNote(
                kanji.kanji,
                kunModelSamples.mapValues { it.value.map(VocabularyItem::surfaceForm) },
                onModelSamples.mapValues { it.value.map(VocabularyItem::surfaceForm) },
                rendakuExceptions,
                radicalDescription,
                idcIndex,
                elementsWithName,
                suitableMeanings,
                translatedSamples.mapKeys { it.key.surfaceForm },
                readingsForSamples,
                metadata.lesson + 1,
                metadata.id + 1,
                kanji.kanken?.toKankenDescription(),
                kanji.jlpt
            )
        }

        private fun radicalDesc(r: Radical): String {
            val listedNames = r.names.singleOrAll()
            return "${r.radical} - $listedNames"
        }

        private fun Int.toKankenDescription(): String {
            val prefix = "Pre-".takeIf { this % 2 == 0 }.orEmpty()

            return when {
                this <= 4 -> prefix + this.minus(this.div(2)).toString()
                else -> this.minus(2).toString()
            }
        }

        private fun List<VocabularyItem>.filterForReadings(baseReading: String): List<VocabularyItem> {
            return this.filter { it.tokens.any { r -> r.normalizedReading == baseReading } }
        }

        private fun List<VocabularyItem>.filterForPrefixReadings(baseReading: String): List<VocabularyItem> {
            return this.filter { it.tokens.any { r -> r.normalizedReading.startsWith(baseReading) } }
        }
    }
}