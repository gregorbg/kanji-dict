package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import com.suushiemaniac.lang.japanese.kanji.model.wadokux.enumeration.SeasonEnum
import com.suushiemaniac.lang.japanese.kanji.model.wadokux.enumeration.SeasonPartEnum
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("seasonword", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuSenseSeason(
    @XmlElement(false) val type: SeasonEnum,
    @XmlElement(false) val part: SeasonPartEnum? = null
)
