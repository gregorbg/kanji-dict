package net.gregorbg.lang.japanese.kanji.model.jisho

import kotlinx.serialization.Serializable

@Serializable
data class JishoAPIResponse(val meta: JishoAPIMeta, val data: List<JishoAPIData>)