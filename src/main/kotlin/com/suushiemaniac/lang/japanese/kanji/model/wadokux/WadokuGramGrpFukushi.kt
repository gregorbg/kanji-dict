package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import com.suushiemaniac.lang.japanese.kanji.model.wadokux.enumeration.TransitivityEnum
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("fukushi", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuGramGrpFukushi(
    @SerialName("ni") val isNi: Boolean? = null,
    @SerialName("to") val isTo: Boolean? = null,
    @SerialName("taru") val isTaru: Boolean? = null,
    @XmlElement(false) val suru: TransitivityEnum? = null
)
