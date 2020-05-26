package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import com.suushiemaniac.lang.japanese.kanji.model.wadokux.util.BooleanStringEncoder
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
@SerialName("text")
data class WadokuEtymologyExplicitText(
    val hasFollowingSpace: @Serializable(with = BooleanStringEncoder::class) Boolean,
    @XmlValue(true) val text: String
)
