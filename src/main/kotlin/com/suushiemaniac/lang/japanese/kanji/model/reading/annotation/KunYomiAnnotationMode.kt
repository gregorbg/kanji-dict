package com.suushiemaniac.lang.japanese.kanji.model.reading.annotation

sealed class KunYomiAnnotationMode {
    abstract fun parse(raw: String): KanjiKunYomi

    abstract val annotationSymbols: List<Char>

    object BracketKunYomiParser : KunYomiAnnotationMode() {
        val BRACKET_PATTERN = """^(.*?)(?:\((.*)\))?$""".toRegex()

        override val annotationSymbols = listOf('(', ')')

        override fun parse(raw: String): KanjiKunYomi {
            val matchedGroups = BRACKET_PATTERN.find(raw)?.groupValues
                ?: error("KanjiKunYomi annotation parsing: $raw did not match the bracket expression!")

            val (core, okuri) = matchedGroups.drop(1)

            return KanjiKunYomi(
                core,
                okuri.takeUnless { it.isEmpty() })
        }
    }

    data class SeparatorKunYomiParser(val separator: String) : KunYomiAnnotationMode() {
        override val annotationSymbols: List<Char>
            get() = separator.toList()

        override fun parse(raw: String): KanjiKunYomi {
            val okuriganaSplits = raw.split(separator)

            return KanjiKunYomi(
                okuriganaSplits.first(),
                okuriganaSplits.getOrNull(1)
            )
        }
    }
}
