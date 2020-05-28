package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import com.suushiemaniac.lang.japanese.kanji.model.wadokux.util.BooleanStringEncoder
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
@XmlSerialName("text", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuText(
    @XmlValue(true) val text: String,
    val hasPrecedingSpace: @Serializable(with = BooleanStringEncoder::class) Boolean = false,
    val hasFollowingSpace: @Serializable(with = BooleanStringEncoder::class) Boolean = false
)
