package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

@Serializable
@SerialName("form")
data class WadokuForm(
    @XmlElement(true) val orth: String,
    val reading: WadokuFormReading
)