package com.suushiemaniac.lang.japanese.kanji.source.jisho

import kotlinx.serialization.Serializable

@Serializable
data class JishoReading(val word: String, val reading: String)