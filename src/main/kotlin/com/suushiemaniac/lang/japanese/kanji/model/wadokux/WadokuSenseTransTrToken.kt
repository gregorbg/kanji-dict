package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
@XmlSerialName("token", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuSenseTransTrToken(
    val genus: String,
    val type: String,
    @XmlValue(true) val token: String
)