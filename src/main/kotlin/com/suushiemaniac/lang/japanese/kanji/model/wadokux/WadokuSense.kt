package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import com.suushiemaniac.lang.japanese.kanji.model.wadokux.enumeration.SeasonEnum
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("sense", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuSense(
    @SerialName("senseid") val senseId: Long? = null,
    @Deprecated("veraltet, siehe seasonword") @XmlElement(false) val season: SeasonEnum? = null,
    val related: Boolean? = null,
    @SerialName("descr") val description: WadokuSenseDescription? = null,
    @XmlElement(true) val accent: List<String> = emptyList(),
    val sense: List<WadokuSense> = emptyList(),
    @SerialName("etym") val etymology: List<WadokuEtymology> = emptyList(),
    @SerialName("usg") val usage: List<WadokuUsage> = emptyList(),
    @SerialName("trans") val translation: List<WadokuSenseTrans> = emptyList(),
    val bracket: List<WadokuSenseBracket> = emptyList(),
    @SerialName("def") val definition: List<WadokuSenseDefinition> = emptyList(),
    @XmlElement(true) @SerialName("birthdeath") val birthDeath: List<String> = emptyList(),
    @XmlElement(true) val date: List<String> = emptyList(),
    @SerialName("expl") val explanation: List<WadokuExplanation> = emptyList(),
    @SerialName("ref") val reference: List<WadokuReference> = emptyList(),
    @SerialName("sref") val specialReference: List<WadokuSpecialReference> = emptyList(),
    val count: List<WadokuCount> = emptyList(),
    @SerialName("seasonword") val seasonWord: List<WadokuSenseSeason> = emptyList(),
    val link: List<WadokuLink> = emptyList()
)
