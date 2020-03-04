package com.suushiemaniac.lang.japanese.kanji.util

import com.atilika.kuromoji.ipadic.Token
import com.atilika.kuromoji.ipadic.Tokenizer

const val SKIP_TOKEN = "_"
val TOKEN_KEYS = listOf("POS-1", "POS-2", "POS-3", "POS-4", "CONJ-TYPE", "CONJ-FORM", "BASE-FORM", "READ", "PRON")

private val tokenizer = Tokenizer()

fun String.tokenizeJapanese(): List<Token> = tokenizer.tokenize(this)