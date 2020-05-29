package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("def", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuSenseDefinition(
    val type: String? = null,
    @SerialName("lang") val language: String? = null,
    @SerialName("langdesc") val languageDescription: String? = null,
    val text: List<WadokuText> = emptyList(),
    val literal: List<WadokuLiteral> = emptyList(),
    @SerialName("transl") val translation: List<String> = emptyList(),
    @SerialName("jap") val japanese: List<String> = emptyList(),
    val iron: List<WadokuIron> = emptyList(),
    val topic: List<String> = emptyList(),
    val token: List<WadokuToken> = emptyList(),
    @SerialName("birthdeath") val birthDeath: List<String> = emptyList(),
    @SerialName("expl") val explanation: List<WadokuExplanation> = emptyList(),
    @SerialName("transcr") val transcription: List<WadokuTranscription> = emptyList(),
    val foreign: List<WadokuForeign> = emptyList(),
    val title: List<WadokuTitle> = emptyList(),
    @SerialName("ref") val reference: List<WadokuReference> = emptyList(),
    @SerialName("emph") val emphasis: List<String> = emptyList(),
    @SerialName("scientif") val scientific: List<WadokuScientific> = emptyList(),
    val bracket: List<WadokuSenseBracket> = emptyList()
)
