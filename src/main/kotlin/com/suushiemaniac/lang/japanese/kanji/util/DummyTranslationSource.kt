package com.suushiemaniac.lang.japanese.kanji.util

import com.suushiemaniac.lang.japanese.kanji.model.reading.token.TokenWithSurfaceForm
import com.suushiemaniac.lang.japanese.kanji.model.vocabulary.VocabTranslation
import com.suushiemaniac.lang.japanese.kanji.source.TranslationSource

object DummyTranslationSource : TranslationSource {
    override fun getTranslationFor(token: TokenWithSurfaceForm) =
        VocabTranslation(DUMMY_TRANSLATION)

    const val DUMMY_TRANSLATION = "TBD"
}
