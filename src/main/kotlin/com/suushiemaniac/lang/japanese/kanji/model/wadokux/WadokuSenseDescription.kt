package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("descr", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuSenseDescription(
    val text: List<WadokuText> = emptyList(),
    @XmlElement(true) @SerialName("jap") val japanese: List<String> = emptyList(),
    @SerialName("transcr") val transcription: List<WadokuTranscription>
)
