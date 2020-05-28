package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("ruigos", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuRuigos(
    val ruigo: List<WadokuRuigo>
)
