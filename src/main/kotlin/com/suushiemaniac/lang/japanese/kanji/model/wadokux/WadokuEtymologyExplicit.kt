package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("expli")
data class WadokuEtymologyExplicit(
    val text: WadokuEtymologyExplicitText,
    val foreign: WadokuEtymologyExplicitForeign
)