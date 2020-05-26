package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("sense")
data class WadokuSense(
    val usg: List<WadokuSenseUsg>,
    val trans: WadokuSenseTrans,
    val ref: WadokuSenseRef
)
