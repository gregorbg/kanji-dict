package net.gregorbg.lang.japanese.kanji.latex

import net.gregorbg.lang.japanese.kanji.latex.model.TexEnvironment
import net.gregorbg.lang.japanese.kanji.model.Kanji
import net.gregorbg.lang.japanese.kanji.model.reading.token.level.SentenceLevelToken
import net.gregorbg.lang.japanese.kanji.model.reading.token.level.WordLevelToken
import net.gregorbg.lang.japanese.kanji.model.vocabulary.VocabularyItem
import net.gregorbg.lang.japanese.kanji.source.KanjiProgressionSource
import net.gregorbg.lang.japanese.kanji.util.*

class TabularWritingKanjiTest<T : WordLevelToken>(
    val testKanji: List<Triple<SentenceLevelToken<T>, VocabularyItem, Kanji>>,
    val sentenceSourceLookup: Map<SentenceLevelToken<T>, String> = emptyMap(),
    val idSource: KanjiProgressionSource? = null
) : TabularKanjiTest() {
    override fun renderBody(isSolution: Boolean): String {
        val idHeader = if (idSource == null) "" else "\\hbox{\\tate 番号} & "
        val headerLine = "${idHeader}文 & 漢字"

        val tableSentenceRows = testKanji.map { (sentence, word, kanji) ->
            val wordPrompt = "\\mbox{\\textbf{（${word.reading}）}}"
            val sentencePrompt = sentence.surfaceForm.replace(word.surfaceForm, wordPrompt)

            val solutionCommand = "\\hphantom".takeUnless { isSolution }.orEmpty()
            val solutionHint = "$solutionCommand{\\Huge ${word.surfaceForm.padEnd(6, '~')}}"

            val idString = if (idSource == null) "" else "${idSource.getOrderingIndexFor(kanji) + 1} & "

            val sentenceSource = sentenceSourceLookup[sentence]
            val sentenceChunk = if (sentenceSource == null) sentencePrompt else "\\href{$sentenceSource}{$sentencePrompt}"

            "$idString$sentenceChunk & $solutionHint\\\\".trim()
        }

        val tableRows = tableSentenceRows.interlace("\\hline")

        val tableMarkup = """
            \hline
            $headerLine\\
            \hline
            \hline
            \endhead
            ${tableRows.joinToString("\n").prependIndent(3, IndentMode.SPACE).trimStart()}
        """.trimIndent()

        val tabular = TexEnvironment(
            "tabularx",
            arguments = listOf(
                "\\linewidth",
                "|sY||c|"
            ),
            body = tableMarkup
        )

        return """
            \setlongtables
            ${tabular.render()}
        """.trimIndent()
    }
}