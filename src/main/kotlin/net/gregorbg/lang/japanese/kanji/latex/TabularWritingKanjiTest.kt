package net.gregorbg.lang.japanese.kanji.latex

import net.gregorbg.lang.japanese.kanji.latex.model.TexEnvironment
import net.gregorbg.lang.japanese.kanji.model.Kanji
import net.gregorbg.lang.japanese.kanji.model.reading.token.level.WordLevelToken
import net.gregorbg.lang.japanese.kanji.model.vocabulary.SampleSentence
import net.gregorbg.lang.japanese.kanji.model.vocabulary.VocabularyItem
import net.gregorbg.lang.japanese.kanji.source.KanjiProgressionSource
import net.gregorbg.lang.japanese.kanji.util.*

class TabularWritingKanjiTest<T : WordLevelToken>(
    val testKanji: List<Triple<SampleSentence<T>, VocabularyItem, Kanji>>,
    val idSource: KanjiProgressionSource
) : TabularKanjiTest() {
    override fun renderBody(isSolution: Boolean): String {
        val headerLine = "\\hbox{\\tate 番号} & 文 & 漢字"

        val tableSentenceRows = testKanji.map { (sentence, word, kanji) ->
            val id = idSource.getOrderingIndexFor(kanji) + 1

            val wordPrompt = "（${word.reading}）"
            val sentencePrompt = sentence.surfaceForm.replace(word.surfaceForm, wordPrompt)

            val solutionHint = word.surfaceForm.takeIf { isSolution }.orEmpty()

            "\$$id\$ & $sentencePrompt & $solutionHint\\\\".trim()
        }

        val tableRows = tableSentenceRows.interlace("\\hline")

        val tableMarkup = """
            \hline
            $headerLine\\
            \hline
            \hline
            ${tableRows.joinToString("\n").prependIndent(3, IndentMode.SPACE).trimStart()}
        """.trimIndent()

        val tabular = TexEnvironment(
            "tabularx",
            arguments = listOf(
                "\\textwidth",
                "|scY|"
            ),
            body = tableMarkup
        )

        val table = TexEnvironment("table", body = tabular.render())

        return table.render()
    }
}