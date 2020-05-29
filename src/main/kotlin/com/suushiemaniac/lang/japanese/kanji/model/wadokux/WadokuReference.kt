package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import com.suushiemaniac.lang.japanese.kanji.model.wadokux.enum.RefTypeEnum
import com.suushiemaniac.lang.japanese.kanji.model.wadokux.enum.SubEntryTypeEnum
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("ref", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuReference(
    val id: Long = 0,
    val senseid: Long? = null,
    @XmlElement(false) val type: RefTypeEnum,
    @XmlElement(false) val subentrytype: SubEntryTypeEnum? = null,
    val text: WadokuText? = null,
    val transcr: WadokuTranscription? = null,
    @XmlElement(true) val jap: String? = null
)
