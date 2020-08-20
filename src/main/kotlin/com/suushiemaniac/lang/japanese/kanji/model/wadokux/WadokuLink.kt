package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import com.suushiemaniac.lang.japanese.kanji.model.wadokux.enumeration.LinkTypeEnum
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
@XmlSerialName("link", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuLink(
    @XmlElement(false) val type: LinkTypeEnum? = null,
    val url: String? = null,
    @XmlValue(true) val link: String = ""
)
