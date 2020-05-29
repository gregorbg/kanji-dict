package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import com.suushiemaniac.lang.japanese.kanji.model.wadokux.enum.LangEnum
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
@XmlSerialName("famn", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuExplanationFamilyName(
    @SerialName("firstname") val isFirstName: Boolean? = null,
    @SerialName("ausn") val isException: Boolean? = null,
    @XmlElement(false) @SerialName("lang") val language: LangEnum? = null,
    @XmlValue(true) @SerialName("famn") val familyName: String
)
