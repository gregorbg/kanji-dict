package com.suushiemaniac.lang.japanese.kanji.source.workbook

import com.suushiemaniac.lang.japanese.kanji.model.Kanji
import com.suushiemaniac.lang.japanese.kanji.model.vocabulary.SampleSentence
import com.suushiemaniac.lang.japanese.kanji.model.vocabulary.VocabularyItem
import com.suushiemaniac.lang.japanese.kanji.model.reading.annotation.KunYomiAnnotationMode
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.TokenWithSurfaceForm
import com.suushiemaniac.lang.japanese.kanji.model.vocabulary.Translation
import com.suushiemaniac.lang.japanese.kanji.model.workbook.SimpleKanji
import com.suushiemaniac.lang.japanese.kanji.model.workbook.WorkbookMetadata
import com.suushiemaniac.lang.japanese.kanji.source.KanjiProgressionSource
import com.suushiemaniac.lang.japanese.kanji.source.SampleSentenceSource
import com.suushiemaniac.lang.japanese.kanji.source.VocabularySource
import com.suushiemaniac.lang.japanese.kanji.source.TranslationSource
import com.suushiemaniac.lang.japanese.kanji.source.workbook.parser.*

class KanjiWorkbookSource private constructor(val bookNum: Int) :
    KanjiProgressionSource, VocabularySource, SampleSentenceSource, TranslationSource {
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
        vocabularyAndSentenceData.mapValues { it.value.map(Triple<VocabularyItem, Translation, SampleSentence?>::first) }
    }

    private val vocabularySupplementData by lazy {
        vocabularyAndSentenceData.values.flatten().groupBy { it.first }
    }

    private val translationData by lazy {
        vocabularySupplementData.mapValues { it.value.map(Triple<VocabularyItem, Translation, SampleSentence?>::second) }
            .mapKeys { it.key.surfaceForm }
    }

    private val sampleSentenceData by lazy {
        vocabularySupplementData.mapValues { it.value.mapNotNull(Triple<VocabularyItem, Translation, SampleSentence?>::third) }
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

    override fun getOrderingIndexFor(kanji: Kanji): Int {
        return getMetadata(kanji)?.id ?: -1
    }

    override fun getVocabularyItemsFor(kanji: Kanji): List<VocabularyItem> {
        return vocabularyData[kanji.kanji.toString()].orEmpty()
    }

    override fun getSampleSentencesFor(vocab: VocabularyItem): List<SampleSentence> {
        return sampleSentenceData[vocab].orEmpty().distinctBy { it.surfaceForm }
    }

    override fun getTranslationFor(token: TokenWithSurfaceForm): Translation? {
        return translationData[token.surfaceForm]?.singleOrNull()
    }

    fun getMetadata(kanji: Kanji): WorkbookMetadata? {
        val lesson = lessonsData[kanji.kanji.toString()] ?: -1
        val id = idData[kanji.kanji.toString()] ?: -1

        if (lesson >= 0 && id >= 0) {
            val idOffset = ORDERED_SYMBOLS.map { it.size }
                .take(bookNum).sum()

            val actualId = idOffset + id
            val bookKey = RESOURCE_NAMES[bookNum]

            return WorkbookMetadata(actualId, lesson, bookKey)
        }

        return null
    }

    companion object {
        private const val RESOURCE_PACKAGE = "15kanji_workbook"

        private const val LESSONS_FILE_TAG = "lessons"
        private const val READINGS_FILE_TAG = "readings"
        private const val SAMPLES_FILE_TAG = "samples"

        private const val ALIGNMENT_SEPARATOR = "."

        private val RESOURCE_NAMES = listOf("beginner_1", "beginner_2", "intermediate_1")

        val ALL_WORKBOOKS = RESOURCE_NAMES.indices.map { lazy { KanjiWorkbookSource(it) } }
        val ORDERED_SYMBOLS = ALL_WORKBOOKS.asSequence().map { it.value.fetchAll() }

        val BEGINNER_JOU by ALL_WORKBOOKS[0]
        val BEGINNER_GE by ALL_WORKBOOKS[1]
        val INTERMEDIATE_JOU by ALL_WORKBOOKS[2]

        private val KUNYOMI_PARSER = KunYomiAnnotationMode.SeparatorKunYomiParser(ALIGNMENT_SEPARATOR)

        private fun loadFile(bookNum: Int, fileTag: String): String {
            val resourceBookName = RESOURCE_NAMES[bookNum]
            val resourcePath = "/$RESOURCE_PACKAGE/$resourceBookName/$fileTag.txt"

            return KanjiWorkbookSource::class.java.getResourceAsStream(resourcePath)
                ?.reader()?.use { it.readText() }.orEmpty()
        }
    }
}
