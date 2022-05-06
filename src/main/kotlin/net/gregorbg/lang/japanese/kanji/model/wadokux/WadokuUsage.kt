package net.gregorbg.lang.japanese.kanji.model.wadokux

import net.gregorbg.lang.japanese.kanji.model.wadokux.enumeration.UsgTypeEnum
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
@XmlSerialName("usg", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuUsage(
    @XmlElement(false) val type: UsgTypeEnum? = null,
    @SerialName("reg") val regular: String? = null,
    @XmlValue(true) val usg: String = ""
)
