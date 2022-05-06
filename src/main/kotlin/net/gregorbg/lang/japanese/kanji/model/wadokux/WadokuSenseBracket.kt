package net.gregorbg.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("bracket", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuSenseBracket(
    val prior: Int? = null,
    @SerialName("meta") val isMeta: Boolean? = null,
    val def: List<WadokuSenseDefinition> = emptyList(),
    @SerialName("expl") val explanation: List<WadokuExplanation> = emptyList(),
    @XmlElement(true) @SerialName("birthdeath") val birthDeath: List<String> = emptyList(),
    @XmlElement(true) val date: List<String> = emptyList(),
    @XmlElement(true) @SerialName("jap") val japanese: List<String> = emptyList(),
    @SerialName("usg") val usage: List<WadokuUsage> = emptyList()
)
