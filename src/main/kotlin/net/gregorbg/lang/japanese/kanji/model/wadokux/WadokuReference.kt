package net.gregorbg.lang.japanese.kanji.model.wadokux

import net.gregorbg.lang.japanese.kanji.model.wadokux.enumeration.RefTypeEnum
import net.gregorbg.lang.japanese.kanji.model.wadokux.enumeration.SubEntryTypeEnum
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("ref", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuReference(
    val id: Long = 0,
    @SerialName("senseid") val senseId: Long? = null,
    @XmlElement(false) val type: RefTypeEnum? = null,
    @XmlElement(false) @SerialName("subentrytype") val subEntryType: SubEntryTypeEnum? = null,
    val text: WadokuText? = null,
    @SerialName("transcr") val transcription: WadokuTranscription? = null,
    @XmlElement(true) @SerialName("jap") val japanese: String? = null
)
