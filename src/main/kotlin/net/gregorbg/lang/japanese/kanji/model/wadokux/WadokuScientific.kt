package net.gregorbg.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("scientif", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuScientific(
    val text: List<WadokuText> = emptyList(),
    val token: List<WadokuToken> = emptyList()
)
