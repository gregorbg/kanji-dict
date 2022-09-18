package net.gregorbg.lang.japanese.kanji.source

import net.gregorbg.lang.japanese.kanji.model.Kanji
import net.gregorbg.lang.japanese.kanji.model.reading.annotation.KanjiReadingAnnotation
import net.gregorbg.lang.japanese.kanji.model.reading.token.KanjiToken
import net.gregorbg.lang.japanese.kanji.model.reading.token.level.SentenceLevelToken
import net.gregorbg.lang.japanese.kanji.model.reading.token.level.WordLevelToken
import net.gregorbg.lang.japanese.kanji.model.vocabulary.SampleSentence
import net.gregorbg.lang.japanese.kanji.model.vocabulary.VocabularyItem

interface SampleSentenceSource<T : WordLevelToken> {
    fun getSampleSentencesFor(vocab: VocabularyItem): List<SentenceLevelToken<T>>

    fun getSampleSentencesFor(kanji: Kanji, reading: KanjiReadingAnnotation): List<SentenceLevelToken<T>> {
        val mockToken = KanjiToken(kanji.kanji, reading.reading, reading.standardisedReading)
        val mockVocab = VocabularyItem(listOf(mockToken))

        return getSampleSentencesFor(mockVocab)
    }
}
