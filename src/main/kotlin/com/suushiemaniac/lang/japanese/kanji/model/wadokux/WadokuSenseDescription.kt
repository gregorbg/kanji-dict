package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("descr", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuSenseDescription(
    val text: List<WadokuText> = emptyList(),
    val jap: List<String> = emptyList(),
    val transcr: List<WadokuTranscription>
)
