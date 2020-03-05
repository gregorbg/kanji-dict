package com.suushiemaniac.lang.japanese.kanji.source.jisho

import kotlinx.serialization.Serializable

@Serializable
data class JishoLink(val text: String, val url: String)