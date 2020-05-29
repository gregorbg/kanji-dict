package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
@XmlSerialName("text", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuText(
    val hasPrecedingSpace: Boolean? = null,
    val hasFollowingSpace: Boolean? = null,
    @XmlValue(true) val text: String = ""
)
