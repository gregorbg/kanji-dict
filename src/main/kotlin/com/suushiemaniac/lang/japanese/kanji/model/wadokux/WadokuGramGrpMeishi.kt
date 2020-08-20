package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import com.suushiemaniac.lang.japanese.kanji.model.wadokux.enumeration.TransitivityEnum
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("meishi", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuGramGrpMeishi(
    @XmlElement(false) val suru: TransitivityEnum? = null
)
