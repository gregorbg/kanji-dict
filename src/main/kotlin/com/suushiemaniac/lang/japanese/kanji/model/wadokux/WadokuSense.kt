package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import com.suushiemaniac.lang.japanese.kanji.model.wadokux.util.BooleanStringEncoder
import com.suushiemaniac.lang.japanese.kanji.model.wadokux.util.LongStringEncoder
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("sense", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuSense( // TODO model as sealed class?
    val senseid: @Serializable(with = LongStringEncoder::class) Long? = null,
    @Deprecated("veraltet, siehe seasonword") val season: String? = null, // seasonEnum
    val related: @Serializable(with = BooleanStringEncoder::class) Boolean = false,
    val descr: WadokuSenseDescription? = null,
    val accent: List<String> = emptyList(),
    // recursion val sense: List<WadokuSense> = emptyList(),
    val etym: List<WadokuEtymology> = emptyList(),
    val usg: List<WadokuUsage> = emptyList(),
    val trans: List<WadokuSenseTrans> = emptyList(),
    val bracket: List<WadokuSenseBracket> = emptyList(),
    val def: List<WadokuSenseDefinition> = emptyList(),
    @XmlElement(true) val birthdeath: List<String> = emptyList(),
    @XmlElement(true) val date: List<String> = emptyList(),
    val expl: List<WadokuExplanation> = emptyList(),
    val ref: List<WadokuReference> = emptyList(),
    val sref: List<WadokuSpecialReference> = emptyList(),
    val count: List<WadokuCount> = emptyList(),
    val seasonword: List<WadokuSenseSeason> = emptyList(),
    val link: List<WadokuLink> = emptyList()
)
