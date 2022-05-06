package net.gregorbg.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("trans", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuSenseTrans(
    @SerialName("lang") val language: String? = null,
    @SerialName("langdesc") val languageDescription: String? = null,
    @SerialName("descr") val description: WadokuSenseDescription? = null,
    @SerialName("usg") val usage: List<WadokuUsage> = emptyList(),
    val tr: WadokuSenseTransTrComplex? = null,
    @SerialName("def") val definition: List<WadokuSenseDefinition> = emptyList(),
    @SerialName("etym") val etymology: WadokuEtymology? = null,
    val title: WadokuTitle? = null
)
