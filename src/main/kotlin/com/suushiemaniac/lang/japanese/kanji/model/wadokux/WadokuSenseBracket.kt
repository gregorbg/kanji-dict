package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("bracket", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuSenseBracket(
    // recursion val def: List<WadokuSenseTransDefinition> = emptyList(),
    @SerialName("expl") val explanation: List<WadokuExplanation> = emptyList(),
    @SerialName("birthdeath") val birthDeath: List<String> = emptyList(),
    val date: List<String> = emptyList(),
    @SerialName("jap") val japanese: List<String> = emptyList(),
    @SerialName("usg") val usage: List<WadokuUsage> = emptyList()
)
