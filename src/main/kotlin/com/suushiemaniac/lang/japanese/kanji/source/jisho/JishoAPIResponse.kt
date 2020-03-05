package com.suushiemaniac.lang.japanese.kanji.source.jisho

import kotlinx.serialization.Serializable

@Serializable
data class JishoAPIResponse(val meta: JishoAPIMeta, val data: List<JishoAPIData>)