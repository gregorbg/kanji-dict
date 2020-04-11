package com.suushiemaniac.lang.japanese.kanji.model.reading.annotation

enum class SinoReadingEra(val symbol: Char) {
    GO('呉'),
    KAN('漢'),
    TOU('唐');

    companion object {
        fun parseSymbol(epochSymbol: Char): SinoReadingEra? {
            return values().find { it.symbol == epochSymbol }
        }
    }
}