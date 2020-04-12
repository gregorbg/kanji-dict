package com.suushiemaniac.lang.japanese.kanji.source

import com.suushiemaniac.lang.japanese.kanji.model.vocabulary.SampleSentence

interface TextSource {
    fun getAvailableIDs(): Set<String>

    fun getText(id: String): List<SampleSentence>
}