package com.suushiemaniac.lang.japanese.kanji.source.workbook

import com.suushiemaniac.lang.japanese.kanji.model.Kanji
import com.suushiemaniac.lang.japanese.kanji.model.SampleSentence
import com.suushiemaniac.lang.japanese.kanji.model.VocabularyItem
import com.suushiemaniac.lang.japanese.kanji.model.reading.KunYomiAnnotationMode
import com.suushiemaniac.lang.japanese.kanji.model.workbook.SimpleKanji
import com.suushiemaniac.lang.japanese.kanji.source.KanjiSource
import com.suushiemaniac.lang.japanese.kanji.source.SampleSentenceSource
import com.suushiemaniac.lang.japanese.kanji.source.VocabularySource
import com.suushiemaniac.lang.japanese.kanji.source.workbook.parser.KunYomiParser
import com.suushiemaniac.lang.japanese.kanji.source.workbook.parser.OnYomiParser
import com.suushiemaniac.lang.japanese.kanji.source.workbook.parser.VocabularyWithSampleSentenceParser

data class KanjiWorkbookSource(val bookNum: Int) : KanjiSource, VocabularySource, SampleSentenceSource {
    private val lessonsContent = loadFile(bookNum, LESSONS_FILE_TAG)
    private val readingsContent = loadFile(bookNum, READINGS_FILE_TAG)
    private val samplesContent = loadFile(bookNum, SAMPLES_FILE_TAG)

    val onYomiParser = OnYomiParser(readingsContent)
    val kunYomiParser = KunYomiParser(readingsContent, KUNYOMI_PARSER)
    val vocabularyParser = VocabularyWithSampleSentenceParser(samplesContent, ALIGNMENT_SEPARATOR)

    val onYomiData by lazy { onYomiParser.getAssociations() }
    val kunYomiData by lazy { kunYomiParser.getAssociations() }
    val vocabularyAndSentenceData by lazy { vocabularyParser.getAssociations() }

    val vocabularyData by lazy {
        vocabularyAndSentenceData.mapValues { it.value.map(Pair<VocabularyItem, SampleSentence?>::first) }
    }

    val sampleSentenceData by lazy {
        vocabularyAndSentenceData.values.flatten().groupBy { it.first.surfaceForm }
            .mapValues { it.value.mapNotNull(Pair<VocabularyItem, SampleSentence?>::second) }
    }

    override fun lookupSymbol(kanji: Char): Kanji? {
        val onYomis = onYomiData[kanji.toString()].orEmpty()
        val kunYomis = kunYomiData[kanji.toString()].orEmpty()

        if (onYomis.isNotEmpty() || kunYomis.isNotEmpty()) {
            return SimpleKanji(kanji, onYomis, kunYomis)
        }

        return null
    }

    override fun fetchAll(): List<Kanji> {
        val allKeys = onYomiData.keys + kunYomiData.keys
        return allKeys.mapNotNull { lookupSymbol(it.first()) }
    }

    override fun getVocabularyItemsFor(kanji: Kanji): List<VocabularyItem> {
        return vocabularyData[kanji.kanji.toString()].orEmpty()
    }

    override fun getSampleSentencesFor(vocab: VocabularyItem): List<SampleSentence> {
        return sampleSentenceData[vocab.surfaceForm].orEmpty().distinctBy { it.rawPhrase }
    }

    companion object {
        const val RESOURCE_PACKAGE = "15kanji_workbook"

        const val LESSONS_FILE_TAG = "lessons"
        const val READINGS_FILE_TAG = "readings"
        const val SAMPLES_FILE_TAG = "samples"

        const val ALIGNMENT_SEPARATOR = "."

        val RESOURCE_NAMES = listOf("beginner_1", "beginner_2", "intermediate_1", "intermediate_2")

        val KUNYOMI_PARSER = KunYomiAnnotationMode.SeparatorKunYomiParser(ALIGNMENT_SEPARATOR)

        fun loadFile(bookNum: Int, fileTag: String): String {
            val resourceBookName = RESOURCE_NAMES[bookNum]
            val resourcePath = "/$RESOURCE_PACKAGE/$resourceBookName/$fileTag.txt"

            return KanjiWorkbookSource::class.java.getResourceAsStream(resourcePath)
                ?.reader()?.use { it.readText() }.orEmpty()
        }
    }
}
