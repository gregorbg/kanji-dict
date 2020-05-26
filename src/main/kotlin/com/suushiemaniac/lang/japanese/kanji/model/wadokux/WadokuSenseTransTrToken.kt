package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
@SerialName("token")
data class WadokuSenseTransTrToken(
    val genus: String,
    val type: String,
    @XmlValue(true) val token: String
)