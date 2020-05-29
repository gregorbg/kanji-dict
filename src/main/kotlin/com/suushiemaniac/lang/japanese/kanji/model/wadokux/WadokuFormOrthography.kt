package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import com.suushiemaniac.lang.japanese.kanji.model.wadokux.enum.OrthTypeEnum
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
@XmlSerialName("orth", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuFormOrthography(
    @SerialName("midashigo") val isMidashigo: Boolean? = null,
    @XmlElement(false) val type: OrthTypeEnum? = null,
    val count: Int? = null,
    @XmlValue(true) @SerialName("orth") val orthography: String
)
