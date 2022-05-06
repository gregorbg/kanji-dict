package net.gregorbg.lang.japanese.kanji.source.util

import net.gregorbg.lang.japanese.kanji.model.Kanji
import net.gregorbg.lang.japanese.kanji.source.KanjiSource

class CombinedKanjiSource(vararg val source: KanjiSource) : KanjiSource {
    override fun lookupSymbol(kanji: Char): Kanji? {
        return source.asSequence()
            .mapNotNull { it.lookupSymbol(kanji) }
            .firstOrNull()
    }
}
