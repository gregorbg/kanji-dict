package net.gregorbg.lang.japanese.kanji.latex

import net.gregorbg.lang.japanese.kanji.latex.model.TexEnvironment
import net.gregorbg.lang.japanese.kanji.model.Kanji
import net.gregorbg.lang.japanese.kanji.model.vocabulary.VocabularyItem
import net.gregorbg.lang.japanese.kanji.source.KanjiProgressionSource
import net.gregorbg.lang.japanese.kanji.util.*
import kotlin.math.ceil

class TabularReadingKanjiTest(
    val testKanji: List<Pair<VocabularyItem, Kanji>>,
    val idSource: KanjiProgressionSource
) : TabularKanjiTest() {
    override fun renderBody(isSolution: Boolean): String {
        val headerGroup = "\\hbox{\\tate 番号} & 単語 & 読み"
        val headerLine = headerGroup.repeatList(NUM_COLUMNS).joinToString(" & ")

        val perColumn = ceil(testKanji.size.toFloat() / NUM_COLUMNS).toInt()

        val columns = testKanji.chunked(perColumn)
        val rows = columns.transpose()

        val tableKanjiRows = rows.map { row ->
            row.joinToString(" & ", postfix = "\\\\") { (voc, kanji) ->
                val id = idSource.getOrderingIndexFor(kanji) + 1
                val solutionHint = voc.reading.takeIf { isSolution }.orEmpty()

                "\$$id\$ & ${voc.surfaceForm} & $solutionHint".trim()
            }
        }

        val tableRows = tableKanjiRows.interlace("\\hline")

        val tableMarkup = """
            \hline
            $headerLine\\
            \hline
            \hline
            ${tableRows.joinToString("\n").prependIndent(3, IndentMode.SPACE).trimStart()}
        """.trimIndent()

        val headerSpecification = "scY".repeatList(NUM_COLUMNS).joinToString("||")

        val tabular = TexEnvironment(
            "tabularx",
            arguments = listOf(
                "\\textwidth",
                "|$headerSpecification|"
            ),
            body = tableMarkup
        )

        val table = TexEnvironment("table", body = tabular.render())

        return table.render()
    }

    companion object {
        const val NUM_COLUMNS = 3
    }
}