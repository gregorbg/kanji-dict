package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import com.suushiemaniac.lang.japanese.kanji.model.wadokux.enum.GodanRow
import com.suushiemaniac.lang.japanese.kanji.model.wadokux.enum.LevelEnum
import com.suushiemaniac.lang.japanese.kanji.model.wadokux.enum.TransitivityEnum
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("doushi", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuGramGrpDoushi(
    @XmlElement(false) val level: LevelEnum? = null,
    @XmlElement(false) val transitivity: TransitivityEnum? = null,
    @XmlElement(false) val godanrow: GodanRow? = null,
    val onbin: Boolean = false
)
