package net.gregorbg.lang.japanese.kanji.model.wadokux.enumeration

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class UsgTypeEnum {
    @SerialName("geo") GEO,
    @SerialName("time") TIME,
    @SerialName("dom") DOM,
    @SerialName("reg") REG,
    @SerialName("style") STYLE,
    @SerialName("plev") PLEV,
    @SerialName("acc") ACC,
    @SerialName("lang") LANG,
    @SerialName("gram") GRAM,
    @SerialName("syn") SYN,
    @SerialName("hyper") HYPER,
    @SerialName("colloc") COLLOC,
    @SerialName("comp") COMP,
    @SerialName("obj") OBJ,
    @SerialName("subj") SUBJ,
    @SerialName("verb") VERB,
    @SerialName("hint") HINT,
    @SerialName("scientific") SCIENTIFIC,
    @SerialName("seasonword") SEASON_WORD,
    @SerialName("familyname") FAMILY_NAME,
    @SerialName("abrev") ABBREVIATION
}