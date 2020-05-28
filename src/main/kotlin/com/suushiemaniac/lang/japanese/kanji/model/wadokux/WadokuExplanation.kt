package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("expl", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuExplanation(
    val options: String? = null,
    val text: List<WadokuText> = emptyList(),
    val literal: List<WadokuLiteral> = emptyList(),
    val transl: List<String> = emptyList(),
    val iron: List<WadokuIron> = emptyList(),
    val topic: List<String> = emptyList(),
    val transcr: List<WadokuTranscription> = emptyList(),
    val token: List<WadokuToken> = emptyList(),
    val jap: List<String> = emptyList(),
    val ref: List<WadokuReference> = emptyList(),
    val foreign: List<WadokuForeign> = emptyList(),
    val title: List<WadokuTitle> = emptyList(),
    val emph: List<String> = emptyList(),
    val specchar: List<String> = emptyList(),
    val famn: List<WadokuExplanationFamilyName> = emptyList(),
    val scientif: List<WadokuScientific> = emptyList()
)
