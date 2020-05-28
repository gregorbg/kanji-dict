package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import com.suushiemaniac.lang.japanese.kanji.model.wadokux.util.IntegerStringEncoder
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("ref", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuSenseRef(
    val id: @Serializable(with = IntegerStringEncoder::class) Int,
    val type: String,
    @XmlElement(true) val transcr: String,
    @XmlElement(true) val jap: String
)
