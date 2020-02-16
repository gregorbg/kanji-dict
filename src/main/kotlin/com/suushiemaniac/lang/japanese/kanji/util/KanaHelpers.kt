package com.suushiemaniac.lang.japanese.kanji.util

import com.mariten.kanatools.KanaConverter

fun String.containsOnlyKatakana() = this == KanaConverter.convertKana(this, KanaConverter.OP_ZEN_HIRA_TO_ZEN_KATA)
fun String.containsOnlyHiragana() = this == KanaConverter.convertKana(this, KanaConverter.OP_ZEN_KATA_TO_ZEN_HIRA)