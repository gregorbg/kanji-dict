package net.gregorbg.lang.japanese.kanji.latex

import net.gregorbg.lang.japanese.kanji.latex.model.Document
import net.gregorbg.lang.japanese.kanji.util.IndentMode
import net.gregorbg.lang.japanese.kanji.util.prependIndent

abstract class DocumentTest {
    fun renderIndividual(isSolution: Boolean = false): Document {
        val body = renderBody(isSolution)
        return renderDocument(body)
    }

    fun renderSeparate() = renderIndividual(false) to renderIndividual(true)

    fun renderCombined(): Document {
        val sentenceQuestionItems = renderBody(false)
        val sentenceSolutionItems = renderBody(true)

        val body = """
            ${sentenceQuestionItems.prependIndent(3, IndentMode.SPACE).trimStart()}
            
            \pagebreak
            
            ${sentenceSolutionItems.prependIndent(3, IndentMode.SPACE).trimStart()}
        """.trimIndent()

        return renderDocument(body)
    }

    abstract fun renderBody(isSolution: Boolean): String
    abstract fun renderDocument(body: String): Document
}