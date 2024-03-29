package net.gregorbg.lang.japanese.kanji.latex

import net.gregorbg.lang.japanese.kanji.model.Kanji
import net.gregorbg.lang.japanese.kanji.model.reading.token.CompoundKanjiToken
import net.gregorbg.lang.japanese.kanji.model.reading.token.KanjiToken
import net.gregorbg.lang.japanese.kanji.model.reading.token.TokenWithSurfaceForm

class SentenceKanjiTest(
    sentences: List<List<TokenWithSurfaceForm>>,
    val read: List<Kanji>,
    val write: List<Kanji>
) : TachikiStyleVerticalTest<TokenWithSurfaceForm>(sentences) {
    val readSymbols
        get() = read.map { it.kanji }

    val writeSymbols
        get() = write.map { it.kanji }

    private val allSymbols
        get() = readSymbols + writeSymbols

    override fun processToken(token: TokenWithSurfaceForm, solution: Boolean): String {
        return when (token) {
            is CompoundKanjiToken -> when {
                solution xor token.manyKanji.all(writeSymbols::contains) -> makeWriteAnnotation(token)
                solution xor token.manyKanji.all(readSymbols::contains) -> makeReadAnnotation(token)
                solution xor allSymbols.intersect(token.manyKanji.asIterable())
                    .isNotEmpty() -> makePartialReadAnnotation(token)
                else -> makeRubyAnnotation(token)
            }
            is KanjiToken -> when {
                solution xor (token.kanji in writeSymbols) -> makeWriteAnnotation(token)
                solution xor (token.kanji in readSymbols) -> makeReadAnnotation(token)
                else -> makeRubyAnnotation(token)
            }
            else -> token.surfaceForm
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
    }
}