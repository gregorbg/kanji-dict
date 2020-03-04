package com.suushiemaniac.lang.japanese.kanji.anki

import com.suushiemaniac.lang.japanese.kanji.model.VocabularyItem
import com.suushiemaniac.lang.japanese.kanji.model.kanjium.Elements
import com.suushiemaniac.lang.japanese.kanji.model.kanjium.KanjiDictEntry
import com.suushiemaniac.lang.japanese.kanji.model.kanjium.Radical
import com.suushiemaniac.lang.japanese.kanji.util.singleOrAll

data class KanjiNote(val kanji: KanjiDictEntry) : AnkiDeckNote {
    override fun getCSVFacts(): List<String> {
        val kanjiSym = this.kanji.kanji

        // TODO
        val kunReadings = emptyMap<String, List<String>>()
        val onReadings = emptyMap<String, List<String>>()

        val radicalText = this.kanji.radicalVariant?.let {
            "${radicalDesc(it)} (variant of ${this.kanji.radical.radical})"
        } ?: radicalDesc(this.kanji.radical)

        val idcGraph = idcImageNum(kanji.idc)
        val idcTag = "<img src=\"idc_$idcGraph.png\"/>"

        val kanjiParts = emptyList<Elements>().single()
        val partsData = kanjiParts.kanjiParts.associateWith { it }
        val partsListing = partsData.entries.joinToString("\r\n") { "${it.key} ${it.value.singleOrAll()}" }

        val sampleWords = emptyList<VocabularyItem>()
        val annotatedTranslations = sampleWords.associateBy({ it.fullText }, { it.translations.singleOrAll() })

        return emptyList()
    }

    override fun getTags(): List<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun radicalDesc(r: Radical): String {
        val listedNames = r.names.singleOrAll()
        return "${r.radical} - $listedNames"
    }

    private fun idcImageNum(idc: String): Int {
        return 0 // FIXME
    }
}