package com.suushiemaniac.lang.japanese.kanji.model.reading

import com.suushiemaniac.lang.japanese.kanji.model.kanjium.enumeration.KunYomi

sealed class KunYomiAnnotationMode {
    abstract fun parse(raw: String): KunYomi

    object BracketKunYomiParser : KunYomiAnnotationMode() {
        val BRACKET_PATTERN = """^(.*?)(?:\((.*)\))?$""".toRegex()

        override fun parse(raw: String): KunYomi {
            val matchedGroups = BRACKET_PATTERN.find(raw)?.groupValues
                ?: error("KunYomi annotation parsing: $raw did not match the bracket expression!")

            val (core, okuri) = matchedGroups.drop(1)
            return KunYomi(core, okuri.takeUnless { it.isEmpty() })
        }
    }

    data class SeparatorKunYomiParser(val separator: String) : KunYomiAnnotationMode() {
        override fun parse(raw: String): KunYomi {
            val okuriganaSplits = raw.split(separator)
            return KunYomi(okuriganaSplits.first(), okuriganaSplits.getOrNull(1))
        }
    }
}
