package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import com.suushiemaniac.lang.japanese.kanji.model.wadokux.enum.GenusEnum
import com.suushiemaniac.lang.japanese.kanji.model.wadokux.enum.NumerusEnum
import com.suushiemaniac.lang.japanese.kanji.model.wadokux.enum.PosEnum
import com.suushiemaniac.lang.japanese.kanji.model.wadokux.util.BooleanStringEncoder
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
@XmlSerialName("token", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuToken(
    @XmlElement(false) val genus: GenusEnum? = null,
    @XmlElement(false) val type: PosEnum? = null,
    @XmlElement(false) val numerus: NumerusEnum? = null,
    val article: @Serializable(with = BooleanStringEncoder::class) Boolean = false,
    val noArticleNecessary: @Serializable(with = BooleanStringEncoder::class) Boolean = false,
    val baseform: String? = null,
    @XmlValue(true) val token: String
)