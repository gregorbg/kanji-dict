package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("katsuyou", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuGramGrpJodoushiKatsuyou(
    val mizenkei: String? = null,
    val renyokei: String? = null,
    val shushikei: String? = null,
    val rentaikei: String? = null,
    val kateikei: String? = null,
    val izenkei: String? = null,
    val meireikei: String? = null
)
