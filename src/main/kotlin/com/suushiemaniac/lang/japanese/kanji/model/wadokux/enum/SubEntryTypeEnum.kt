package com.suushiemaniac.lang.japanese.kanji.model.wadokux.enum

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SubEntryTypeEnum {
    @SerialName("head") HEAD,
    @SerialName("tail") TAIL,
    @SerialName("sa") SA,
    @SerialName("suru") SURU,
    @SerialName("saseru") SASERU,
    @SerialName("shita") SHITA,
    @SerialName("shite") SHITE,
    @SerialName("teki") TEKI,
    @SerialName("to") TO,
    @SerialName("taru") TARU,
    @SerialName("na") NA,
    @SerialName("ni") NI,
    @SerialName("no") NO,
    @SerialName("da") DA,
    @SerialName("de") DE,
    @SerialName("e") E,
    @SerialName("o") O,
    @SerialName("kara") KARA,
    @SerialName("garu") GARU,
    @SerialName("ge") GE,
    @SerialName("ku") KU,
    @SerialName("mi") MI,
    @SerialName("VwBsp") USAGE_EXAMPLE,
    @SerialName("WIdiom") IDIOMATIC,
    @SerialName("XSatz") PHRASE,
    @SerialName("ZSprW") PROVERB,
    @SerialName("other") OTHER
}