package com.suushiemaniac.lang.japanese.kanji.model.kanjivg.enumeration

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ZoomAndPan {
    @SerialName("disable") DISABLE,
    @SerialName("magnify") MAGNIFY,
    @SerialName("zoom") ZOOM
}
