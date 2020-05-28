package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("gramGrp", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuGramGrp(
    @Deprecated("veraltet, nach Migration auf Schemaversion 1.3 entfernen") val pos: List<String> = emptyList(),
    val meishi: WadokuGramGrpMeishi? = null,
    val doushi: List<WadokuGramGrpDoushi> = emptyList(),
    val jodoushi: WadokuGramGrpJodoushi? = null,
    val jokeiyoushi: WadokuGramGrpJokeiyoushi? = null,
    val joshi: WadokuGramGrpJoshi? = null,
    val fukushi: WadokuGramGrpFukushi? = null,
    val keiyoushi: WadokuGramGrpKeiyoushi? = null,
    val keiyoudoushi: WadokuGramGrpKeiyoudoushi? = null,
    val kandoushi: WadokuGramGrpKandoushi? = null,
    val prefix: WadokuGramGrpPrefix? = null,
    val suffix: WadokuGramGrpSuffix? = null,
    val rentaishi: WadokuGramGrpRentaishi? = null,
    val setsuzkushi: WadokuGramGrpSetsuzokushi? = null,
    val daimeishi: WadokuGramGrpDaimeishi? = null,
    val setsuzkujoshi: WadokuGramGrpSetsuzokujoshi? = null,
    val shuujoshi: WadokuGramGrpShuujoshi? = null,
    val kakarijoshi: WadokuGramGrpKakarijoshi? = null,
    val kakujoshi: WadokuGramGrpKakujoshi? = null,
    val fukujoshi: WadokuGramGrpFukujoshi? = null,
    val wordcomponent: WadokuGramGrpWordComponent? = null,
    val specialcharacter: WadokuGramGrpSpecialCharacter? = null,
    val kanji: WadokuGramGrpKanji? = null,
    val rengo: WadokuGramGrpRengo? = null
)