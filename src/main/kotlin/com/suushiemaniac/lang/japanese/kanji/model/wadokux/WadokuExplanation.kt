package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("expl", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuExplanation(
    val options: String? = null,
    val text: List<WadokuText> = emptyList(),
    val literal: List<WadokuLiteral> = emptyList(),
    @XmlElement(true) @SerialName("transl") val translation: List<String> = emptyList(),
    val iron: List<WadokuIron> = emptyList(),
    @XmlElement(true) val topic: List<String> = emptyList(),
    @SerialName("transcr") val transcription: List<WadokuTranscription> = emptyList(),
    val token: List<WadokuToken> = emptyList(),
    @XmlElement(true) @SerialName("jap") val japanese: List<String> = emptyList(),
    @SerialName("ref") val reference: List<WadokuReference> = emptyList(),
    val foreign: List<WadokuForeign> = emptyList(),
    val title: List<WadokuTitle> = emptyList(),
    @XmlElement(true) @SerialName("emph") val emphasis: List<String> = emptyList(),
    @XmlElement(true) @SerialName("specchar") val specialCharacter: List<String> = emptyList(),
    @SerialName("famn") val familyName: List<WadokuExplanationFamilyName> = emptyList(),
    @SerialName("scientif") val scientific: List<WadokuScientific> = emptyList()
)
