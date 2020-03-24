package com.suushiemaniac.lang.japanese.kanji.model.reading.type

sealed class KunYomiAnnotationMode {
    abstract fun parse(raw: String): KunYomi

    abstract val annotationSymbols: List<Char>

    object BracketKunYomiParser : KunYomiAnnotationMode() {
        val BRACKET_PATTERN = """^(.*?)(?:\((.*)\))?$""".toRegex()

        override val annotationSymbols = listOf('(', ')')

        override fun parse(raw: String): KunYomi {
            val matchedGroups = BRACKET_PATTERN.find(raw)?.groupValues
                ?: error("KunYomi annotation parsing: $raw did not match the bracket expression!")

            val (core, okuri) = matchedGroups.drop(1)
            return KunYomi(
                core,
                okuri.takeUnless { it.isEmpty() })
        }
    }

    data class SeparatorKunYomiParser(val separator: String) : KunYomiAnnotationMode() {
        override val annotationSymbols: List<Char>
            get() = separator.toList()

        override fun parse(raw: String): KunYomi {
            val okuriganaSplits = raw.split(separator)
            return KunYomi(
                okuriganaSplits.first(),
                okuriganaSplits.getOrNull(1)
            )
        }
    }
}
