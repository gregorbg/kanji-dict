package com.suushiemaniac.lang.japanese.kanji.source

import com.suushiemaniac.lang.japanese.kanji.model.reading.token.TokenWithSurfaceForm
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.compose.CompositeTokens
import com.suushiemaniac.lang.japanese.kanji.model.vocabulary.ComplexText

interface ComplexTextSource<T : ComplexText<out TokenWithSurfaceForm>> {
    fun getAvailableIDs(): Set<String>

    fun getText(id: String): T
}