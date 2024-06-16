package net.gregorbg.lang.japanese.kanji.source.twentytwo

import net.gregorbg.lang.japanese.kanji.model.Kanji
import net.gregorbg.lang.japanese.kanji.model.reading.token.MorphologyToken
import net.gregorbg.lang.japanese.kanji.model.vocabulary.SampleSentence
import net.gregorbg.lang.japanese.kanji.model.vocabulary.Translation
import net.gregorbg.lang.japanese.kanji.model.vocabulary.VocabularyItem
import net.gregorbg.lang.japanese.kanji.source.KanjiProgressionSource
import net.gregorbg.lang.japanese.kanji.source.VocabularySource
import net.gregorbg.lang.japanese.kanji.source.workbook.parser.NumericalIndexParser
import net.gregorbg.lang.japanese.kanji.source.workbook.parser.VocabularyWithSampleSentenceParser
import net.gregorbg.lang.japanese.kanji.util.invert
import java.io.File

object TwentyTwoJitenSource : KanjiProgressionSource, VocabularySource {
    private const val LESSONS_FILE_TAG = "jiten"
    private const val SAMPLES_FILE_TAG = "samples"

    private const val ALIGNMENT_SEPARATOR = "."

    var BASE_PATH = "/please/change/this/"

    private val lessonsContent by lazy { loadFile(LESSONS_FILE_TAG) }
    private val samplesContent by lazy { loadFile(SAMPLES_FILE_TAG) }

    private val idParser by lazy { NumericalIndexParser(lessonsContent) }
    private val idData by lazy { idParser.getAssociations() }

    private val vocabularyParser by lazy { VocabularyWithSampleSentenceParser(samplesContent, ALIGNMENT_SEPARATOR) }
    private val vocabularyAndSentenceData by lazy { vocabularyParser.getAssociations() }
    private val vocabularyData by lazy {
        vocabularyAndSentenceData.mapValues { it.value.map(Triple<VocabularyItem, Translation, SampleSentence<MorphologyToken>?>::first) }
    }

    override fun lookupSymbol(kanji: Char): Kanji? {
        return null // TODO
    }

    override fun lookupIndex(index: Int): Kanji? {
        return idData.invert()[index]?.let { lookupSymbol(it.first()) }
    }

    override fun fetchAll(): List<Kanji> {
        return idData.keys.mapNotNull { lookupSymbol(it.first()) }
    }

    override fun getOrderingIndexFor(kanji: Kanji): Int {
        return idData[kanji.kanji.toString()] ?: -1
    }

    override fun getVocabularyItemsFor(kanji: Kanji): List<VocabularyItem> {
        return vocabularyData[kanji.kanji.toString()].orEmpty()
    }

    private fun loadFile(fileTag: String): String {
        val resourcePath = "${BASE_PATH.trimEnd('/')}/$fileTag.txt"
        return File(resourcePath).takeIf { it.exists() }?.reader()?.use { it.readText() }.orEmpty()
    }
}