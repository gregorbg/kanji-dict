package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import com.suushiemaniac.lang.japanese.kanji.model.wadokux.enum.OrthTypeEnum
import com.suushiemaniac.lang.japanese.kanji.model.wadokux.util.BooleanStringEncoder
import com.suushiemaniac.lang.japanese.kanji.model.wadokux.util.IntegerStringEncoder
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
@XmlSerialName("orth", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuFormOrth(
    val midashigo: @Serializable(with = BooleanStringEncoder::class) Boolean = false,
    @XmlElement(false) val type: OrthTypeEnum? = null,
    val count: @Serializable(with = IntegerStringEncoder::class) Int? = null,
    @XmlValue(true) val orth: String
)
