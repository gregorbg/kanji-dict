package net.gregorbg.lang.japanese.kanji.source

import net.gregorbg.lang.japanese.kanji.model.Kanji
import net.gregorbg.lang.japanese.kanji.model.vocabulary.VocabularyItem

interface VocabularySource {
    fun getVocabularyItemsFor(kanji: Kanji): List<VocabularyItem>
}
