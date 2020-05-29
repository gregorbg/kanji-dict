package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import com.suushiemaniac.lang.japanese.kanji.model.wadokux.enum.GenusEnum
import com.suushiemaniac.lang.japanese.kanji.model.wadokux.enum.NumerusEnum
import com.suushiemaniac.lang.japanese.kanji.model.wadokux.enum.PosEnum
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
@XmlSerialName("token", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuToken(
    @XmlElement(false) @SerialName("genus") val gender: GenusEnum? = null,
    @XmlElement(false) @SerialName("type") val partOfSpeech: PosEnum? = null,
    @XmlElement(false) @SerialName("numerus") val number: NumerusEnum? = null,
    val article: Boolean? = null,
    val noArticleNecessary: Boolean? = null,
    @SerialName("baseform") val baseForm: String? = null,
    @XmlValue(true) val token: String
)