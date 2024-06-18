package net.gregorbg.lang.japanese.kanji.latex

import net.gregorbg.lang.japanese.kanji.latex.model.Document
import net.gregorbg.lang.japanese.kanji.latex.model.EnumCommand
import net.gregorbg.lang.japanese.kanji.latex.model.TexCommand
import net.gregorbg.lang.japanese.kanji.latex.model.TexEnvironment
import net.gregorbg.lang.japanese.kanji.latex.model.types.DocumentClass
import net.gregorbg.lang.japanese.kanji.latex.model.types.UsePackage
import net.gregorbg.lang.japanese.kanji.model.reading.token.*
import net.gregorbg.lang.japanese.kanji.util.*

abstract class TachikiStyleVerticalTest<T : TokenWithSurfaceForm>(val sentences: List<List<T>>): DocumentTest() {
    override fun renderBody(isSolution: Boolean): String {
        val sentenceItems = generateSentenceItems(isSolution)
        return renderEnumeration(sentenceItems)
    }

    override fun renderDocument(body: String): Document {
        return compileDocument(body)
    }

    private fun generateSentenceItems(solution: Boolean): List<String> {
        return sentences.map { sentence ->
            sentence.joinToString("") { processToken(it, solution) }
                .ensureEndsWith(FULLSTOP_KUTOTEN)
                .replace("}\\", "}~\\")
                .replace("\\d+".toRegex()) { "\\rensuji{${it.value}}" }
        }
    }

    protected abstract fun processToken(token: T, solution: Boolean): String

    companion object {
        private fun compileDocument(body: String): Document {
            return Document(
                EnumCommand(DocumentClass.LTJ_ARTICLE_TATE, "12pt,landscape"),
                listOf(
                    EnumCommand(UsePackage.LUATEXJA),
                    EnumCommand(UsePackage.LUATEXJA_RUBY),
                    EnumCommand(UsePackage.LUATEXJA_TATE_GEOMETRY),
                    EnumCommand(UsePackage.LUATEXJA_EXTENSIONS),
                    EnumCommand(UsePackage.ENUMITEM),
                    EnumCommand(UsePackage.XCOLOR),
                    EnumCommand(UsePackage.GEOMETRY, "left=10mm,top=7mm,bottom=7mm,right=14mm"),
                ),
                listOf(
                    TexCommand("linespread", listOf("2.8")),
                    TexCommand("pagenumbering", listOf("gobble"))
                ),
                body
            )
        }

        private fun renderEnumeration(items: List<String>): String {
            val itemsBody = """
                \LARGE
                ${items.joinToString("\n") { "\\item $it" }.prependIndent(4, IndentMode.SPACE).trimStart()}
            """.trimIndent()

            val environment = TexEnvironment(
                "enumerate",
                options = "label=\\rensuji{\\Large\\protect\\textcircled{\\large\\arabic*}}",
                body = itemsBody
            )

            return environment.render()
        }
    }
}