package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("bracket", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuSenseBracket(
    // recursion val def: List<WadokuSenseTransDefinition> = emptyList(),
    val expl: List<WadokuExplanation> = emptyList(),
    val birthdeath: List<String> = emptyList(),
    val date: List<String> = emptyList(),
    val jap: List<String> = emptyList(),
    val usg: List<WadokuUsage> = emptyList()
)
