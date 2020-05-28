package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("title", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuTitle(
    val orig: Boolean,
    val lang: String, // langEnum
    val abbrev: Boolean,
    val text: List<WadokuText> = emptyList(),
    val token: List<WadokuToken> = emptyList(),
    val emph: List<String> = emptyList()
)
