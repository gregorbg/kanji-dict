package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("etym", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuEtymology(
    val langdesc: String? = null,
    val origin: String? = null,
    val text: List<WadokuText> = emptyList(),
    val ref: List<WadokuReference> = emptyList(),
    val literal: List<WadokuLiteral> = emptyList(),
    @XmlElement(true) val emph: List<String> = emptyList(),
    @XmlElement(true) val transl: List<String> = emptyList(),
    val transcr: List<WadokuTranscription> = emptyList(),
    @XmlElement(true) val topic: List<String> = emptyList(),
    val foreign: List<WadokuForeign> = emptyList(),
    @XmlElement(true) val impli: List<String> = emptyList(),
    val expli: List<WadokuEtymologyExplicit> = emptyList(),
    @XmlElement(true) val jap: List<String> = emptyList(),
    val title: List<WadokuTitle> = emptyList(),
    val abbrev: WadokuEtymologyAbbreviation? = null
)