package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("trans")
data class WadokuSenseTrans(
    val tr: WadokuSenseTransTr
)
