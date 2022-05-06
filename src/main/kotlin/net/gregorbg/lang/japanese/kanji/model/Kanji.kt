package net.gregorbg.lang.japanese.kanji.model

import net.gregorbg.lang.japanese.kanji.model.reading.annotation.KanjiKunYomi
import net.gregorbg.lang.japanese.kanji.model.reading.annotation.KanjiOnYomi

interface Kanji {
    val kanji: Char
    val onYomi: List<KanjiOnYomi>
    val kunYomi: List<KanjiKunYomi>
}