package net.gregorbg.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("reading", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuFormReading(
    val count: Int? = null,
    @XmlElement(true) @SerialName("hira") val hiragana: String,
    @XmlElement(true) val hatsuon: String,
    val romaji: WadokuFormReadingRomaji? = null,
    @XmlElement(true) val accent: List<String> = emptyList()
)