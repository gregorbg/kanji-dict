package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("form", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuForm(
    @SerialName("orth") val orthography: List<WadokuFormOrthography>,
    @SerialName("pron") val pronounciation: List<WadokuFormPronounciation> = emptyList(),
    val reading: WadokuFormReading? = null
)