package net.gregorbg.lang.japanese.kanji.source

import net.gregorbg.lang.japanese.kanji.model.reading.token.level.TextLevelToken
import net.gregorbg.lang.japanese.kanji.model.reading.token.level.WordLevelToken

interface ComplexTextSource<out T : WordLevelToken> {
    fun getAvailableIDs(): Set<String>

    fun getText(id: String): TextLevelToken<T>
}