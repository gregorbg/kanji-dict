package com.suushiemaniac.lang.japanese.kanji.model.vocabulary

enum class VocabTagModifier(val kana: String) {
    VERB_SURU("する"),
    ADJECTIVE_NA("な");

    companion object {
        fun fromKana(kana: String): VocabTagModifier? {
            return values().find { it.kana == kana }
        }
    }
}
