package com.suushiemaniac.lang.japanese.kanji.latex

import com.suushiemaniac.lang.japanese.kanji.model.reading.token.*
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.compose.CompositeTokens

abstract class TachikiStyleVerticalTest<T : TokenWithSurfaceForm>(val sentences: List<CompositeTokens<out T>>) {
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
            val tokens = tokenizeSentence(sentence)

            tokens.joinToString("") {
                processToken(it, solution)
            }.replace("}\\", "}~\\")
        }
    }

    protected abstract fun tokenizeSentence(sentence: CompositeTokens<out T>): List<T>

    protected abstract fun processToken(token: T, solution: Boolean): String

    companion object {
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