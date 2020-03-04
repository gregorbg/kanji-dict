package com.suushiemaniac.lang.japanese.kanji.model

import com.atilika.kuromoji.ipadic.Token
import com.suushiemaniac.lang.japanese.kanji.util.tokenizeJapanese

data class SampleSentence(val rawPhrase: String) {
    fun parseTokens(): List<Token> {
        return this.rawPhrase.tokenizeJapanese()
    }
}