package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("def", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuSenseDefinition(
    val type: String? = null,
    val lang: String? = null,
    val langdesc: String? = null,
    val text: List<WadokuText> = emptyList(),
    val literal: List<WadokuLiteral> = emptyList(),
    val transl: List<String> = emptyList(),
    val jap: List<String> = emptyList(),
    val iron: List<WadokuIron> = emptyList(),
    val topic: List<String> = emptyList(),
    val token: List<WadokuToken> = emptyList(),
    val birthdeath: List<String> = emptyList(),
    val expl: List<WadokuExplanation> = emptyList(),
    val transcr: List<WadokuTranscription> = emptyList(),
    val foreign: List<WadokuForeign> = emptyList(),
    val title: List<WadokuTitle> = emptyList(),
    val ref: List<WadokuReference> = emptyList(),
    val emph: List<String> = emptyList(),
    val scientif: List<WadokuScientific> = emptyList(),
    val bracket: List<WadokuSenseBracket> = emptyList()
)
