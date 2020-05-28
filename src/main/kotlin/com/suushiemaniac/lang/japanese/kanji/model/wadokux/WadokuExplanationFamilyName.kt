package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import com.suushiemaniac.lang.japanese.kanji.model.wadokux.util.BooleanStringEncoder
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
@XmlSerialName("famn", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuExplanationFamilyName(
    val firstname: @Serializable(with = BooleanStringEncoder::class) Boolean = false,
    val ausn: @Serializable(with = BooleanStringEncoder::class) Boolean = false,
    val lang: String? = null, // langEnum
    @XmlValue(true) val famn: String
)
