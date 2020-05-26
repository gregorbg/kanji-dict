package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
@SerialName("usg")
data class WadokuSenseUsg(
    val type: String,
    @XmlValue(true) val usg: String
)
