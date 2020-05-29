package com.suushiemaniac.lang.japanese.kanji.model.wadokux.enum

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class GodanRow {
    @SerialName("wa") WA,
    @SerialName("wa_o") WA_O,
    @SerialName("ba") BA,
    @SerialName("ga") GA,
    @SerialName("ka") KA,
    @SerialName("ka_i_yu") KA_I_YU,
    @SerialName("ra") RA,
    @SerialName("ra_i") RA_I,
    @SerialName("na") NA,
    @SerialName("ma") MA,
    @SerialName("sa") SA,
    @SerialName("ta") TA
}