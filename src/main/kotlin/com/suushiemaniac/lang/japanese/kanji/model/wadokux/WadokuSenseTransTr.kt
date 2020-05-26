package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("tr")
data class WadokuSenseTransTr(
    val token: WadokuSenseTransTrToken
)
