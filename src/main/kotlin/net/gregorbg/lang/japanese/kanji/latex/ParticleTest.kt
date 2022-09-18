package net.gregorbg.lang.japanese.kanji.latex

import net.gregorbg.lang.japanese.kanji.model.reading.token.*
import net.gregorbg.lang.japanese.kanji.model.reading.token.level.WordLevelToken

class ParticleTest(sentences: List<List<WordLevelToken>>) :
    TachikiStyleVerticalTest<WordLevelToken>(sentences) {
    override fun processToken(token: WordLevelToken, solution: Boolean): String {
        return if (token is MorphologyToken && token.isParticle)
            if (solution) makeSolutionAnnotation(token) else makeWriteAnnotation(token)
        else token.surfaceForm
    }

    companion object {
        private val MorphologyToken.isParticle
            get() = (morphology?.posLevels?.getOrNull(0) == "助詞") && (morphology.posLevels.getOrNull(1) != "接続助詞")

        fun makeWriteAnnotation(token: TokenWithSurfaceForm) =
            "(${"　".repeat(token.surfaceForm.length)})"

        fun makeSolutionAnnotation(token: TokenWithSurfaceForm) =
            "\\kasen{${token.surfaceForm}}"
    }
}
