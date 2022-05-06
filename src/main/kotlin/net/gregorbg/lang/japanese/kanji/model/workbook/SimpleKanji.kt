package net.gregorbg.lang.japanese.kanji.model.workbook

import net.gregorbg.lang.japanese.kanji.model.Kanji
import net.gregorbg.lang.japanese.kanji.model.reading.annotation.KanjiKunYomi
import net.gregorbg.lang.japanese.kanji.model.reading.annotation.KanjiOnYomi

data class SimpleKanji(
    override val kanji: Char,
    override val onYomi: List<KanjiOnYomi>,
    override val kunYomi: List<KanjiKunYomi>
) : Kanji