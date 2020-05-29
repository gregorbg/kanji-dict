package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("gramGrp", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuGramGrp(
    @Deprecated("veraltet, nach Migration auf Schemaversion 1.3 entfernen") val pos: List<String> = emptyList(),
    val meishi: WadokuGramGrpMeishi? = null,
    val doushi: List<WadokuGramGrpDoushi> = emptyList(),
    val jodoushi: WadokuGramGrpJodoushi? = null,
    @XmlElement(true) val jokeiyoushi: String? = null,
    @XmlElement(true) val joshi: String? = null,
    val fukushi: WadokuGramGrpFukushi? = null,
    val keiyoushi: WadokuGramGrpKeiyoushi? = null,
    val keiyoudoushi: WadokuGramGrpKeiyoudoushi? = null,
    @XmlElement(true) val kandoushi: String? = null,
    @XmlElement(true) val prefix: String? = null,
    @XmlElement(true) val suffix: String? = null,
    @XmlElement(true) val rentaishi: String? = null,
    @XmlElement(true) val setsuzkushi: String? = null,
    @XmlElement(true) val daimeishi: String? = null,
    @XmlElement(true) val setsuzkujoshi: String? = null,
    @XmlElement(true) val shuujoshi: String? = null,
    @XmlElement(true) val kakarijoshi: String? = null,
    @XmlElement(true) val kakujoshi: String? = null,
    @XmlElement(true) val fukujoshi: String? = null,
    @XmlElement(true) @SerialName("wordcomponent") val wordComponent: String? = null,
    @XmlElement(true) @SerialName("specialcharacter") val specialCharacter: String? = null,
    @XmlElement(true) val kanji: String? = null,
    @XmlElement(true) val rengo: String? = null
)