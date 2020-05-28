package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("trans", WadokuExportEntry.NAMESPACE, WadokuExportEntry.NS_PREFIX)
data class WadokuSenseTrans(
    val lang: String? = null,
    val langdesc: String? = null,
    val descr: WadokuSenseDescription? = null,
    val usg: List<WadokuUsage> = emptyList(),
    val tr: WadokuSenseTransTrComplex? = null,
    val def: List<WadokuSenseDefinition> = emptyList(),
    val etym: WadokuEtymology? = null,
    val title: WadokuTitle? = null
)
