package net.gregorbg.lang.japanese.kanji.model.wadokux.enumeration

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PosEnum {
    @SerialName("N") NOUN,
    @SerialName("V") VERB,
    @SerialName("Adj") ADJECTIVE,
    @SerialName("Adn") ADNOUN,
    @SerialName("Adv") ADVERB,
    @SerialName("BspSatz") EXAMPLE_PHRASE,
    @SerialName("Hilfsv") AUXILIARY_VERB,
    @SerialName("Interj") INTERJECTION,
    @SerialName("Kanji") SINGLE_KANJI,
    @SerialName("Konj") CONJUNCTION,
    @SerialName("Part") PARTICLE,
    @SerialName("Praef") PREFIX,
    @SerialName("Pron") PRONOUN,
    @SerialName("Suff") SUFFIX,
    @SerialName("Zus") COMPOSITION,
    @SerialName("Redensart") IDIOM,
    @SerialName("Wortkomp") WORD_COMPOUND,
    @SerialName("Sonderzeichen") SYMBOL,
    @SerialName("Themenpart") TOPIC_PARTICLE,
    @SerialName("Sonderform") IRREGULAR,
    @SerialName("Undef") UNDEFINED
}