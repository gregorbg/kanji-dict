package com.suushiemaniac.lang.japanese.kanji.source.workbook

import com.suushiemaniac.lang.japanese.kanji.model.Kanji
import com.suushiemaniac.lang.japanese.kanji.model.SampleSentence
import com.suushiemaniac.lang.japanese.kanji.model.VocabularyItem
import com.suushiemaniac.lang.japanese.kanji.model.reading.type.KunYomiAnnotationMode
import com.suushiemaniac.lang.japanese.kanji.model.workbook.SimpleKanji
import com.suushiemaniac.lang.japanese.kanji.model.workbook.WorkbookMetadata
import com.suushiemaniac.lang.japanese.kanji.source.KanjiSource
import com.suushiemaniac.lang.japanese.kanji.source.SampleSentenceSource
import com.suushiemaniac.lang.japanese.kanji.source.VocabularySource
import com.suushiemaniac.lang.japanese.kanji.source.workbook.parser.*

data class KanjiWorkbookSource(val bookNum: Int) : KanjiSource, VocabularySource, SampleSentenceSource {
    private val lessonsContent = loadFile(bookNum, LESSONS_FILE_TAG)
    private val readingsContent = loadFile(bookNum, READINGS_FILE_TAG)
    private val samplesContent = loadFile(bookNum, SAMPLES_FILE_TAG)

    private val onYomiParser = OnYomiParser(readingsContent)
    private val kunYomiParser = KunYomiParser(readingsContent, KUNYOMI_PARSER)
    private val vocabularyParser = VocabularyWithSampleSentenceParser(samplesContent, ALIGNMENT_SEPARATOR)
    private val lessonsParser = LessonsParser(lessonsContent)
    private val idParser = NumericalIndexParser(lessonsContent)

    private val onYomiData by lazy { onYomiParser.getAssociations() }
    private val kunYomiData by lazy { kunYomiParser.getAssociations() }
    private val vocabularyAndSentenceData by lazy { vocabularyParser.getAssociations() }
    private val lessonsData by lazy { lessonsParser.getAssociations() }
    private val idData by lazy { idParser.getAssociations() }

    private val vocabularyData by lazy {
        vocabularyAndSentenceData.mapValues { it.value.map(Pair<VocabularyItem, SampleSentence?>::first) }
    }

    private val sampleSentenceData by lazy {
        vocabularyAndSentenceData.values.flatten().groupBy { it.first.withoutTranslations() }
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
        return idData.keys.mapNotNull { lookupSymbol(it.first()) }
    }

    override fun getVocabularyItemsFor(kanji: Kanji): List<VocabularyItem> {
        return vocabularyData[kanji.kanji.toString()].orEmpty()
    }

    override fun getSampleSentencesFor(vocab: VocabularyItem): List<SampleSentence> {
        return sampleSentenceData[vocab.withoutTranslations()].orEmpty().distinctBy { it.rawPhrase }
    }

    fun getMetadata(kanji: Kanji): WorkbookMetadata? {
        val lesson = lessonsData[kanji.kanji.toString()] ?: -1
        val id = idData[kanji.kanji.toString()] ?: -1

        if (lesson >= 0 && id >= 0) {
            val idOffset = SYMBOL_COUNTS.take(bookNum).sum()
            val actualId = idOffset + id

            val bookKey = RESOURCE_NAMES[bookNum]

            return WorkbookMetadata(actualId, lesson, bookKey)
        }

        return null
    }

    companion object {
        const val RESOURCE_PACKAGE = "15kanji_workbook"

        const val LESSONS_FILE_TAG = "lessons"
        const val READINGS_FILE_TAG = "readings"
        const val SAMPLES_FILE_TAG = "samples"

        const val ALIGNMENT_SEPARATOR = "."

        val RESOURCE_NAMES = listOf("beginner_1", "beginner_2", "intermediate_1")
        val SYMBOL_COUNTS by lazy { RESOURCE_NAMES.indices.map { KanjiWorkbookSource(it).fetchAll().size } }

        val KUNYOMI_PARSER = KunYomiAnnotationMode.SeparatorKunYomiParser(ALIGNMENT_SEPARATOR)

        private fun loadFile(bookNum: Int, fileTag: String): String {
            val resourceBookName = RESOURCE_NAMES[bookNum]
            val resourcePath = "/$RESOURCE_PACKAGE/$resourceBookName/$fileTag.txt"

            return KanjiWorkbookSource::class.java.getResourceAsStream(resourcePath)
                ?.reader()?.use { it.readText() }.orEmpty()
        }
    }
}
