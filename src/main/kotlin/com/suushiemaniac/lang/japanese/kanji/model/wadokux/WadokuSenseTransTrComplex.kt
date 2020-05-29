package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("tr", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuSenseTransTrComplex(
    val text: List<WadokuText> = emptyList(),
    val token: List<WadokuToken> = emptyList(),
    @SerialName("def") val definition: List<WadokuSenseDefinition> = emptyList(),
    @SerialName("expl") val explanation: List<WadokuExplanation> = emptyList(),
    val literal: List<WadokuLiteral> = emptyList(),
    @SerialName("famn") val familyName: List<WadokuExplanationFamilyName> = emptyList(),
    @SerialName("emph") val emphasis: List<String> = emptyList(),
    @SerialName("transl") val translation: List<String> = emptyList(),
    @SerialName("specchar") val specialCharacter: List<String> = emptyList(),
    val iron: List<WadokuIron> = emptyList(),
    val topic: List<String> = emptyList(),
    val foreign: List<WadokuForeign> = emptyList(),
    val title: List<WadokuTitle> = emptyList(),
    @SerialName("jap") val japanese: List<String> = emptyList(),
    @SerialName("transcription") val transcription: List<WadokuTranscription> = emptyList(),
    val bracket: List<WadokuSenseBracket> = emptyList(),
    val count: List<WadokuCount> = emptyList()
)
