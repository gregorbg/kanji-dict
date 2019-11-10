package com.suushiemaniac.lang.japanese.kanji.util

import com.mariten.kanatools.KanaConverter

val String.isKatakana get() = this == KanaConverter.convertKana(this, KanaConverter.OP_ZEN_HIRA_TO_ZEN_KATA)
val String.isHiragana get() = this == KanaConverter.convertKana(this, KanaConverter.OP_ZEN_KATA_TO_ZEN_HIRA)