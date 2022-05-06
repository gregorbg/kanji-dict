package net.gregorbg.lang.japanese.kanji.source

import net.gregorbg.lang.japanese.kanji.model.Kanji

interface KanjiSource {
    fun lookupSymbol(kanji: Char): Kanji?
}