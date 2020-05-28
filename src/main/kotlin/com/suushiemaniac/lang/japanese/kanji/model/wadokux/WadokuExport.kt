package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import com.suushiemaniac.lang.japanese.kanji.model.wadokux.util.WadokuDateStringEncoder
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import java.time.ZonedDateTime

@Serializable
@XmlSerialName("entries", "", "")
data class WadokuExport(
    val date: @Serializable(with = WadokuDateStringEncoder::class) ZonedDateTime,
    @XmlSerialName("entry", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX) val entries: List<WadokuExportEntry>
)