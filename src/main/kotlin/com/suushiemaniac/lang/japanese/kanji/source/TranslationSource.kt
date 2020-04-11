package com.suushiemaniac.lang.japanese.kanji.source

import com.suushiemaniac.lang.japanese.kanji.model.reading.token.TokenWithSurfaceForm
import com.suushiemaniac.lang.japanese.kanji.model.vocabulary.VocabTranslation

interface TranslationSource {
    fun getTranslationFor(token: TokenWithSurfaceForm): VocabTranslation?
}
