package com.suushiemaniac.lang.japanese.kanji.source

import com.suushiemaniac.lang.japanese.kanji.model.reading.token.ReadingToken

interface TextSource {
    fun getAvailableIDs(): Set<String>

    fun getText(id: String): ReadingToken
}