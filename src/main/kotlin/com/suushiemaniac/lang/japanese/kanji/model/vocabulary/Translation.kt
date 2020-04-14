package com.suushiemaniac.lang.japanese.kanji.model.vocabulary

data class Translation(
    val mainTranslation: String,
    val otherTranslations: List<String> = NO_TRANSLATIONS
) {
    companion object {
        private val NO_TRANSLATIONS = emptyList<String>()
    }
}