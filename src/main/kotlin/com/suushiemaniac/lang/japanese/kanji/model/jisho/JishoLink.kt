package com.suushiemaniac.lang.japanese.kanji.model.jisho

import kotlinx.serialization.Serializable

@Serializable
data class JishoLink(val text: String, val url: String)