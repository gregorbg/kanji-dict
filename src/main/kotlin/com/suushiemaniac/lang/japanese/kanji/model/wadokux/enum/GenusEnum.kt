package com.suushiemaniac.lang.japanese.kanji.model.wadokux.enum

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class GenusEnum {
    @SerialName("m") MASC,
    @SerialName("f") FEM,
    @SerialName("n") NEUT,
    @SerialName("mn") MASC_NEUT,
    @SerialName("mf") MASC_FEM,
    @SerialName("nf") NEUT_FEM,
    @SerialName("mnf") MASC_NEUT_FEM
}