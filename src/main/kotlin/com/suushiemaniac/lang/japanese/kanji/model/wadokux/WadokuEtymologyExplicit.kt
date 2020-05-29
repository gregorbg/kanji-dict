package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("expli", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuEtymologyExplicit(
    val text: List<WadokuText> = emptyList(),
    val foreign: List<WadokuForeign> = emptyList()
)