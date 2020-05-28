package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import com.suushiemaniac.lang.japanese.kanji.model.wadokux.util.BooleanStringEncoder
import com.suushiemaniac.lang.japanese.kanji.model.wadokux.util.LongStringEncoder
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("entry", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuExportEntry(
    val id: @Serializable(with = LongStringEncoder::class) Long,
    val version: String,
    @SerialName("HE") val isMainEntry: @Serializable(with = BooleanStringEncoder::class) Boolean = false,
    val form: WadokuForm,
    val etym: List<WadokuEtymology> = emptyList(),
    val gramGrp: WadokuGramGrp? = null,
    val usg: List<WadokuUsage> = emptyList(),
    val sense: List<WadokuSense> = emptyList(),
    val expl: List<WadokuExplanation> = emptyList(),
    val count: List<WadokuCount> = emptyList(),
    val ref: List<WadokuReference> = emptyList(),
    val sref: List<WadokuSpecialReference> = emptyList(),
    val link: List<WadokuLink> = emptyList(),
    @XmlElement(true) val steinhaus: List<String> = emptyList(),
    @XmlElement(true) val wikide: String? = null,
    @XmlElement(true) val wikija: String? = null,
    val ruigos: WadokuRuigos? = null
) {
    companion object {
        const val NAMESPACE = "http://www.wadoku.de/xml/entry"
        const val NS_PREFIX = ""
    }
}