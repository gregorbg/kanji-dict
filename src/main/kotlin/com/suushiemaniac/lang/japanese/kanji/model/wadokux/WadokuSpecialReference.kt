package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import com.suushiemaniac.lang.japanese.kanji.model.wadokux.util.IntegerStringEncoder
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("sref", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuSpecialReference(
    val id: @Serializable(with = IntegerStringEncoder::class) Int,
    val type: String, // refTypeEnum
    val index: @Serializable(with = IntegerStringEncoder::class) Int
)
