package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import com.suushiemaniac.lang.japanese.kanji.model.wadokux.enumeration.LangEnum
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("title", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuTitle(
    @SerialName("orig") val isOriginal: Boolean? = null,
    @XmlElement(false) @SerialName("lang") val language: LangEnum? = null,
    @SerialName("abbrev") val isAbbreviation: Boolean? = null,
    val text: List<WadokuText> = emptyList(),
    val token: List<WadokuToken> = emptyList(),
    @XmlElement(true) @SerialName("emph") val emphasis: List<String> = emptyList()
)
