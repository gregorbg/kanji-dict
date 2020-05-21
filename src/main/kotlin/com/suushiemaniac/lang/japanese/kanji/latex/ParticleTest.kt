package com.suushiemaniac.lang.japanese.kanji.latex

import com.suushiemaniac.lang.japanese.kanji.model.reading.token.*
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.compose.CompositeTokens
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.compose.CompositeWordLevelTokens
import com.suushiemaniac.lang.japanese.kanji.util.flatten

class ParticleTest(sentences: List<CompositeWordLevelTokens<out WordLevelToken>>) :
    TachikiStyleVerticalTest<WordLevelToken>(sentences) {
    override fun tokenizeSentence(sentence: CompositeTokens<out WordLevelToken>): List<WordLevelToken> {
        return sentence.tokens.flatMap { it.flatten() }
    }

    override fun processToken(token: WordLevelToken, solution: Boolean): String {
        return if (token is MorphologyToken && token.isParticle)
            if (solution) makeSolutionAnnotation(token) else makeWriteAnnotation(token)
        else token.surfaceForm
    }

    companion object {
        private val MorphologyToken.isParticle
            get() = morphology?.posLevels?.firstOrNull() == "助詞"

        fun makeWriteAnnotation(token: TokenWithSurfaceForm) =
            "(${"　".repeat(token.surfaceForm.length)})"

        fun makeSolutionAnnotation(token: TokenWithSurfaceForm) =
            "\\kasen{${token.surfaceForm}}"
    }
}