package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import com.suushiemaniac.lang.japanese.kanji.model.wadokux.enum.PronTypeEnum
import com.suushiemaniac.lang.japanese.kanji.model.wadokux.util.IntegerStringEncoder
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("pron", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
@Deprecated("veraltet, nach Migration auf Schemaversion 1.3 entfernen")
data class WadokuFormPronounciation(
    @XmlElement(false) val type: PronTypeEnum? = null,
    val orth: @Serializable(with = IntegerStringEncoder::class) Int? = null,
    val accent: @Serializable(with = IntegerStringEncoder::class) Int? = null,
    val altaccent: @Serializable(with = IntegerStringEncoder::class) Int? = null,
    val count: @Serializable(with = IntegerStringEncoder::class) Int? = null,
    @Deprecated("deprecated") val text: List<WadokuText> = emptyList(),
    @XmlElement(true) val dev: List<String> = emptyList()
)
