package com.suushiemaniac.lang.japanese.kanji.source

import com.suushiemaniac.lang.japanese.kanji.model.SampleSentence
import com.suushiemaniac.lang.japanese.kanji.model.VocabularyItem

interface SampleSentenceSource {
    fun getSampleSentencesFor(vocab: VocabularyItem): List<SampleSentence>
}
