package net.gregorbg.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("jodoushi", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuGramGrpJodoushi(
    val katsuyou: List<WadokuGramGrpJodoushiKatsuyou> = emptyList()
)
