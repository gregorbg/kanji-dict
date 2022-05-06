package net.gregorbg.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("count", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuCount(
    val text: List<WadokuText> = emptyList(),
    val ref: List<WadokuReference> = emptyList()
)
