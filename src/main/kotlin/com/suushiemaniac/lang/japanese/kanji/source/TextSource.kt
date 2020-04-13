package com.suushiemaniac.lang.japanese.kanji.source

import com.suushiemaniac.lang.japanese.kanji.model.vocabulary.Text

interface TextSource {
    fun getAvailableIDs(): Set<String>

    fun getText(id: String): Text
}