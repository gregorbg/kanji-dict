package com.suushiemaniac.lang.japanese.kanji.model.jisho

import com.suushiemaniac.lang.japanese.kanji.model.jisho.JishoAPIData
import com.suushiemaniac.lang.japanese.kanji.model.jisho.JishoAPIMeta
import kotlinx.serialization.Serializable

@Serializable
data class JishoAPIResponse(val meta: JishoAPIMeta, val data: List<JishoAPIData>)