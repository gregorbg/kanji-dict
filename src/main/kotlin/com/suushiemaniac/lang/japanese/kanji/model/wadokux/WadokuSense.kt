package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import com.suushiemaniac.lang.japanese.kanji.model.wadokux.enum.SeasonEnum
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("sense", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuSense( // TODO model as sealed class?
    val senseid: Long? = null,
    @Deprecated("veraltet, siehe seasonword") @XmlElement(false) val season: SeasonEnum? = null,
    val related: Boolean? = null,
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
