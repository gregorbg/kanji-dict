package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

@Serializable
@SerialName("etym")
data class WadokuEtymology(
    @XmlElement(true) val impli: String,
    val expli: WadokuEtymologyExplicit
)