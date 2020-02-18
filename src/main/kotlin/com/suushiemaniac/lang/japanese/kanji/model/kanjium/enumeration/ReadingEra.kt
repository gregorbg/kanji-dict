package com.suushiemaniac.lang.japanese.kanji.model.kanjium.enumeration

enum class ReadingEra(val symbol: Char) {
    GO('呉'),
    KAN('漢'),
    TOU('唐');

    companion object {
        fun parseSymbol(epochSymbol: Char): ReadingEra? {
            return values().find { it.symbol == epochSymbol }
        }
    }
}