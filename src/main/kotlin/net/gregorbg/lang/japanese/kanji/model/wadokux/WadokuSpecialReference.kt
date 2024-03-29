package net.gregorbg.lang.japanese.kanji.model.wadokux

import net.gregorbg.lang.japanese.kanji.model.wadokux.enumeration.RefTypeEnum
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
@XmlSerialName("sref", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuSpecialReference(
    val id: Int? = null,
    @XmlElement(false) val type: RefTypeEnum? = null,
    val index: Int? = null,
    @XmlValue(true) val sref: String
)
