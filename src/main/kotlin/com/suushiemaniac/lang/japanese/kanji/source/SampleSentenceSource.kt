package com.suushiemaniac.lang.japanese.kanji.source

import com.suushiemaniac.lang.japanese.kanji.model.vocabulary.SampleSentence
import com.suushiemaniac.lang.japanese.kanji.model.vocabulary.VocabularyItem

interface SampleSentenceSource {
    fun getSampleSentencesFor(vocab: VocabularyItem): List<SampleSentence>
}
