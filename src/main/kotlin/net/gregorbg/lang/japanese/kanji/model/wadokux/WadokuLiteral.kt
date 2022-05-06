package net.gregorbg.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("literal", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuLiteral(
    val text: List<WadokuText> = emptyList(),
    val token: List<WadokuToken> = emptyList()
)
