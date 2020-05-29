package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import com.suushiemaniac.lang.japanese.kanji.model.wadokux.enum.PosEnum
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("pos", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
@Deprecated("deprecated")
data class WadokuGramGrpPosComplex(
    val type: PosEnum,
    val text: List<WadokuText> = emptyList(),
    @SerialName("transcr") val transcription: List<WadokuTranscription> = emptyList(),
    @SerialName("expl") val explanation: List<WadokuExplanation> = emptyList()
)
