package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("fukushi", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuGramGrpFukushi(
    val ni: Boolean = false,
    val to: Boolean = false,
    val taru: Boolean = false,
    val suru: String? = null // transitivityEnum
)
