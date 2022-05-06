package net.gregorbg.lang.japanese.kanji.source

import net.gregorbg.lang.japanese.kanji.model.reading.token.TokenWithSurfaceForm
import net.gregorbg.lang.japanese.kanji.model.vocabulary.Translation

interface TranslationSource {
    fun getTranslationFor(token: TokenWithSurfaceForm): Translation?
}
