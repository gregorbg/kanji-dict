package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("doushi", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuGramGrpDoushi(
    val level: String? = null, // levelEnum
    val transitivity: String? = null, // transiticityEnum
    val godanrow: String? = null, // restricted inline
    val onbin: Boolean = false
)
