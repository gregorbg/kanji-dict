package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("tr", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuSenseTransTrComplex(
    val text: List<WadokuText> = emptyList(),
    val token: List<WadokuToken> = emptyList(),
    val def: List<WadokuSenseDefinition> = emptyList(),
    val expl: List<WadokuExplanation> = emptyList(),
    val literal: List<WadokuLiteral> = emptyList(),
    val famn: List<WadokuExplanationFamilyName> = emptyList(),
    val emph: List<String> = emptyList(),
    val transl: List<String> = emptyList(),
    val specchar: List<String> = emptyList(),
    val iron: List<WadokuIron> = emptyList(),
    val topic: List<String> = emptyList(),
    val foreign: List<WadokuForeign> = emptyList(),
    val title: List<WadokuTitle> = emptyList(),
    val jap: List<String> = emptyList(),
    val transcr: List<WadokuTranscription> = emptyList(),
    val bracket: List<WadokuSenseBracket> = emptyList(),
    val count: List<WadokuCount> = emptyList()
)
