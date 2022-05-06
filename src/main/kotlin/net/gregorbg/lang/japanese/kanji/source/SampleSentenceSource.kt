package net.gregorbg.lang.japanese.kanji.source

import net.gregorbg.lang.japanese.kanji.model.Kanji
import net.gregorbg.lang.japanese.kanji.model.reading.annotation.KanjiReadingAnnotation
import net.gregorbg.lang.japanese.kanji.model.reading.token.KanjiToken
import net.gregorbg.lang.japanese.kanji.model.vocabulary.SampleSentence
import net.gregorbg.lang.japanese.kanji.model.vocabulary.VocabularyItem

interface SampleSentenceSource {
    fun getSampleSentencesFor(vocab: VocabularyItem): List<SampleSentence>

    fun getSampleSentencesFor(kanji: Kanji, reading: KanjiReadingAnnotation): List<SampleSentence> {
        val mockToken = KanjiToken(kanji.kanji, reading.reading, reading.standardisedReading)
        val mockVocab = VocabularyItem(listOf(mockToken))

        return getSampleSentencesFor(mockVocab)
    }
}
