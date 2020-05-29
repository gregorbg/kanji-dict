package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("etym", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuEtymology(
    @SerialName("langdesc") val languageDescription: String? = null,
    val origin: String? = null,
    val text: List<WadokuText> = emptyList(),
    @SerialName("ref") val reference: List<WadokuReference> = emptyList(),
    val literal: List<WadokuLiteral> = emptyList(),
    @XmlElement(true) @SerialName("emph") val emphasis: List<String> = emptyList(),
    @XmlElement(true) @SerialName("trans") val translation: List<String> = emptyList(),
    @SerialName("transcr") val transcription: List<WadokuTranscription> = emptyList(),
    @XmlElement(true) val topic: List<String> = emptyList(),
    val foreign: List<WadokuForeign> = emptyList(),
    @XmlElement(true) @SerialName("impli") val implicit: List<String> = emptyList(),
    @SerialName("expli") val explicit: List<WadokuEtymologyExplicit> = emptyList(),
    @XmlElement(true) @SerialName("jap") val japanese: List<String> = emptyList(),
    val title: List<WadokuTitle> = emptyList(),
    @SerialName("abbrev") val abbreviation: WadokuEtymologyAbbreviation? = null
)