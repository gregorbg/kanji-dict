package net.gregorbg.lang.japanese.kanji.util

import net.gregorbg.lang.japanese.kanji.model.reading.token.TokenWithSurfaceForm
import net.gregorbg.lang.japanese.kanji.model.vocabulary.Translation
import net.gregorbg.lang.japanese.kanji.source.TranslationSource

object DummyTranslationSource : TranslationSource {
    override fun getTranslationFor(token: TokenWithSurfaceForm) =
        Translation(DUMMY_TRANSLATION)

    const val DUMMY_TRANSLATION = "TBD"
}
