package com.suushiemaniac.lang.japanese.kanji.model.wadokux

import com.suushiemaniac.lang.japanese.kanji.model.wadokux.util.BooleanStringEncoder
import com.suushiemaniac.lang.japanese.kanji.model.wadokux.util.IntegerStringEncoder
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WadokuExportEntry(
    val id: @Serializable(with = IntegerStringEncoder::class) Int,
    val version: String,
    @SerialName("HE") val he: @Serializable(with = BooleanStringEncoder::class) Boolean,
    val form: WadokuForm,
    val etym: WadokuEtymology,
    val gramGrp: WadokuGramGrp,
    val sense: WadokuSense
)