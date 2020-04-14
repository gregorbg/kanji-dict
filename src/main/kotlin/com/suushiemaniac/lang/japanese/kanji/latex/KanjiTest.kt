package com.suushiemaniac.lang.japanese.kanji.latex

import com.suushiemaniac.lang.japanese.kanji.model.Kanji
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.CompoundKanjiToken
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.KanjiToken
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.SymbolToken
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.TokenWithSurfaceForm
import com.suushiemaniac.lang.japanese.kanji.util.flatten

class KanjiTest(val sentences: List<SymbolToken>, val read: List<Kanji>, val write: List<Kanji>) {
    val readSymbols
        get() = read.map { it.kanji }

    val writeSymbols
        get() = write.map { it.kanji }

    private val allSymbols
        get() = readSymbols + writeSymbols

    fun renderIndividual(solution: Boolean = false): String {
        val sentenceItems = generateSentenceItems(solution)
        val body = renderEnumeration(sentenceItems)

        return renderDocument(body)
    }

    fun renderSeparate() =
        renderIndividual(false) to renderIndividual(true)

    fun renderCombined(): String {
        val sentenceQuestionItems = generateSentenceItems(false)
        val sentenceSolutionItems = generateSentenceItems(true)

        val body = """
            ${renderEnumeration(sentenceQuestionItems)}
            \pagebreak
            ${renderEnumeration(sentenceSolutionItems)}
        """

        return renderDocument(body)
    }

    private fun generateSentenceItems(solution: Boolean): List<String> {
        return sentences.map { sentence ->
            sentence.flatten().joinToString("") {
                when (it) {
                    is CompoundKanjiToken -> when {
                        solution xor it.manyKanji.all(writeSymbols::contains) -> makeWriteAnnotation(it)
                        solution xor it.manyKanji.all(readSymbols::contains) -> makeReadAnnotation(it)
                        solution xor allSymbols.intersect(it.manyKanji.asIterable())
                            .isNotEmpty() -> makePartialReadAnnotation(it)
                        else -> makeRubyAnnotation(it)
                    }
                    is KanjiToken -> when {
                        solution xor (it.kanji in writeSymbols) -> makeWriteAnnotation(it)
                        solution xor (it.kanji in readSymbols) -> makeReadAnnotation(it)
                        else -> makeRubyAnnotation(it)
                    }
                    else -> it.surfaceForm
                }
            }.replace("}\\", "}~\\")
        }
    }

    fun makePartialReadAnnotation(token: CompoundKanjiToken): String {
        val symbolParts = token.manyKanji.map {
            if (it in allSymbols) "\\kasen{${it}}" else it.toString()
        }

        return symbolParts.joinToString("")
    }

    companion object {
        fun makeWriteAnnotation(token: TokenWithSurfaceForm) =
            "\\kasen{${token.reading}}"

        fun makeReadAnnotation(token: TokenWithSurfaceForm) =
            "\\kasen{${token.surfaceForm}}"

        fun makeRubyAnnotation(token: TokenWithSurfaceForm) =
            "\\ruby{${token.surfaceForm}}{${token.reading}}"

        private fun renderDocument(body: String): String {
            return """
                \documentclass[12pt,landscape]{ltjtarticle}

                \usepackage{luatexja}
                \usepackage{luatexja-ruby}
                \usepackage{lltjext}
                \usepackage{enumitem}
                \usepackage{xcolor}
                \usepackage[left=10mm,top=7mm,bottom=7mm,right=14mm]{geometry}

                \linespread{2.8}
                \pagenumbering{gobble}

                \begin{document}
                $body
                \end{document}
            """.trimIndent()
        }

        private fun renderEnumeration(items: List<String>): String {
            return """
                \LARGE
                \begin{enumerate}[label=\rensuji{\Large\protect\textcircled{\large\arabic*}}]
            	    ${items.joinToString("\n") { "\\item $it" }}
                \end{enumerate}
            """
        }
    }
}