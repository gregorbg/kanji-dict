package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
@XmlSerialName("daimeishi", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuGramGrpDaimeishi(
    @XmlValue(true) val daimeishi: String? = null
)
