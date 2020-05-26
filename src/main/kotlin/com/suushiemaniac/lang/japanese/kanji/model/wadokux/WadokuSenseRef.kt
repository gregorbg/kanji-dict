package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import com.suushiemaniac.lang.japanese.kanji.model.wadokux.util.IntegerStringEncoder
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

@Serializable
@SerialName("ref")
data class WadokuSenseRef(
    val id: @Serializable(with = IntegerStringEncoder::class) Int,
    val type: String,
    @XmlElement(true) val transcr: String,
    @XmlElement(true) val jap: String
)
