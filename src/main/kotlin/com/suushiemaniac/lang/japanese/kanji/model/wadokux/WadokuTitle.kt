package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import com.suushiemaniac.lang.japanese.kanji.model.wadokux.enum.LangEnum
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("title", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuTitle(
    val orig: Boolean = false,
    @XmlElement(false) val lang: LangEnum? = null,
    val abbrev: Boolean = false,
    val text: List<WadokuText> = emptyList(),
    val token: List<WadokuToken> = emptyList(),
    val emph: List<String> = emptyList()
)
