package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import com.suushiemaniac.lang.japanese.kanji.model.wadokux.util.BooleanStringEncoder
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
@XmlSerialName("token", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuToken(
    val genus: String? = null, // genusEnum
    val type: String? = null, // posEnum
    val numerus: String? = null, // numerusEnum
    val article: @Serializable(with = BooleanStringEncoder::class) Boolean = false,
    val noArticleNecessary: @Serializable(with = BooleanStringEncoder::class) Boolean = false,
    val baseform: String? = null,
    @XmlValue(true) val token: String
)