package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("gramGrp")
data class WadokuGramGrp(
    val meishi: WadokuGramGrpMeishi
)