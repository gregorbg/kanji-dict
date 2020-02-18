package com.suushiemaniac.lang.japanese.kanji.model.kanjium.enumeration

enum class KanjiType {
    COMPOUND_IDEOGRAPH,
    PHONO_SEMANTIC_COMPOUND,
    PICTOGRAPH,
    IDEOGRAPH;

    companion object {
        fun parse(typeRepresentation: String): KanjiType {
            val nameRep = typeRepresentation.toUpperCase()
                .replace(' ', '_')
                .replace('-', '_')

            return valueOf(nameRep)
        }
    }
}
