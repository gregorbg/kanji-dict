package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

@Serializable
@SerialName("reading")
data class WadokuFormReading(
    @XmlElement(true) val hira: String,
    @XmlElement(true) val hatsuon: String,
    @XmlElement(true) val accent: Int
)