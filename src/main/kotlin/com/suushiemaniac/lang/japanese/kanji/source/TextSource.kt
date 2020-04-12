package com.suushiemaniac.lang.japanese.kanji.source

import com.suushiemaniac.lang.japanese.kanji.model.reading.token.TokenWithSurfaceForm

interface TextSource {
    fun getAvailableIDs(): Set<String>

    fun getText(id: String): TokenWithSurfaceForm
}