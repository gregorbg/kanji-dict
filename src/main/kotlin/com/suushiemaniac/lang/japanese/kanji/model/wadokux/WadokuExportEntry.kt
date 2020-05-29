package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("entry", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuExportEntry(
    val id: Long,
    val version: String,
    @SerialName("HE") val isMainEntry: Boolean? = null,
    val form: WadokuForm,
    @SerialName("etym") val etymology: List<WadokuEtymology> = emptyList(),
    val gramGrp: WadokuGramGrp? = null,
    @SerialName("usg") val usage: List<WadokuUsage> = emptyList(),
    val sense: List<WadokuSense> = emptyList(),
    @SerialName("expl") val explanation: List<WadokuExplanation> = emptyList(),
    val count: List<WadokuCount> = emptyList(),
    @SerialName("ref") val reference: List<WadokuReference> = emptyList(),
    @SerialName("sref") val specialReference: List<WadokuSpecialReference> = emptyList(),
    val link: List<WadokuLink> = emptyList(),
    @XmlElement(true) val steinhaus: List<String> = emptyList(),
    @XmlElement(true) @SerialName("wikide") val wikiDe: String? = null,
    @XmlElement(true) @SerialName("wikija") val wikiJa: String? = null,
    val ruigos: WadokuRuigos? = null
) {
    companion object {
        const val NAMESPACE = "http://www.wadoku.de/xml/entry"
        const val NS_PREFIX = ""
    }
}