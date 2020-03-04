package com.suushiemaniac.lang.japanese.kanji.anki

import com.suushiemaniac.lang.japanese.kanji.anki.AnkiExporter.JSON
import com.suushiemaniac.lang.japanese.kanji.anki.model.RubyFuriganaFormatter
import com.suushiemaniac.lang.japanese.kanji.model.kanjium.Elements
import com.suushiemaniac.lang.japanese.kanji.model.kanjium.KanjiDictEntry
import com.suushiemaniac.lang.japanese.kanji.model.kanjium.Radical
import com.suushiemaniac.lang.japanese.kanji.model.kanjium.enumeration.KunYomi
import com.suushiemaniac.lang.japanese.kanji.model.kanjium.enumeration.OnYomi
import com.suushiemaniac.lang.japanese.kanji.model.reading.KanjiReading
import com.suushiemaniac.lang.japanese.kanji.model.reading.ReadingWithSurfaceForm
import com.suushiemaniac.lang.japanese.kanji.util.singleOrAll
import com.suushiemaniac.lang.japanese.kanji.util.toHiragana
import com.suushiemaniac.lang.japanese.kanji.util.toKatakana
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer

data class KanjiNote(
    val kanjiSymbol: Char,
    val kunReadingsWithSamples: Map<KunYomi, List<String>>,
    val onReadingsWithSamples: Map<OnYomi, List<String>>,
    val radicalDescription: String,
    val idcGraphNum: Int,
    val elementsWithName: Map<Char, String>,
    val coreMeaning: String,
    val sampleTranslations: Map<String, String>,
    val sampleReadings: Map<String, ReadingWithSurfaceForm>,
    val lesson: Int,
    val id: Int,
    val kanken: String?,
    val jlpt: Int?
) : AnkiDeckNote {
    override fun getCSVFacts(): List<String> {
        val ankiHackReadings = sampleReadings.mapValues { it.value.asFurigana(RubyFuriganaFormatter) }
            .mapValues {
                it.value.replace(
                    RubyFuriganaFormatter.format(KanjiReading(kanjiSymbol, it.key)),
                    " $kanjiSymbol "
                ).trim()
            }

        return listOf(
            kanjiSymbol.toString(),
            JSON.stringify(
                MapSerializer(String.serializer(), ListSerializer(String.serializer())),
                kunReadingsWithSamples.mapKeys { it.key.coreReading.toHiragana() }),
            JSON.stringify(
                MapSerializer(String.serializer(), ListSerializer(String.serializer())),
                onReadingsWithSamples.mapKeys { it.key.kanaReading.toKatakana() }),
            radicalDescription,
            "<img src=\"idcGraph-$idcGraphNum.png\">",
            elementsWithName.entries.joinToString("\r\n") { "${it.key} ${it.value}" },
            coreMeaning,
            JSON.stringify(MapSerializer(String.serializer(), String.serializer()), sampleTranslations),
            JSON.stringify(MapSerializer(String.serializer(), String.serializer()), ankiHackReadings)
        )
    }

    override fun getTags(): List<String> {
        return listOfNotNull("L$lesson", "id-$id", jlpt?.let { "JLPT-N$it" }, kanken?.let { "Kanken:$it" })
    }

    companion object {
        // https://raw.githubusercontent.com/mifunetoshiro/kanjium/master/data/idc_mappingtable.txt
        val IDC_GRAPH_MAPPING =
            listOf("⿰", "⿱", "⿲", "⿳", "⿴", "⿵", "⿶", "⿷", "⿸", "⿹", "⿺", "囗", "品", "品u", "品l", "品r", "⿱1", "⿰4", "⿰5", "⿰1", "⿰2", "⿰3")

        fun from(kanji: KanjiDictEntry, elements: Elements, lesson: Int, id: Int): KanjiNote {
            val radicalDescription =
                kanji.radicalVariant?.let { radicalDesc(it) + "(Variante von " + radicalDesc(kanji.radical) + ")" }
                    ?: radicalDesc(kanji.radical)

            val idcIndex = IDC_GRAPH_MAPPING.indexOf(kanji.idc).takeUnless { it == -1 }?.let { it + 1 } ?: 0

            val elementsWithName = mapOf<Char, String>()

            val suitableMeanings = kanji.compactMeaning.takeUnless { it.isEmpty() }?.joinToString()
                ?: elements.compactMeaning

            val translatedSamples = mapOf<String, String>() // FIXME
            val readingsForSamples = mapOf<String, ReadingWithSurfaceForm>() // FIXME

            return KanjiNote(
                kanji.kanji,
                kanji.kunYomi.associateWith { emptyList<String>() }, // FIXME
                kanji.onYomi.associateWith { emptyList<String>() }, // FIXME
                radicalDescription,
                idcIndex,
                elementsWithName,
                suitableMeanings,
                translatedSamples,
                readingsForSamples,
                lesson,
                id,
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
    }
}