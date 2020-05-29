package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("form", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuForm(
    val orth: List<WadokuFormOrth>,
    val pron: List<WadokuFormPronounciation> = emptyList(),
    val reading: WadokuFormReading? = null
)