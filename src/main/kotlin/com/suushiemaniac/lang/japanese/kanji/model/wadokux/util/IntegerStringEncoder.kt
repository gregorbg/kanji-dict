package com.suushiemaniac.lang.japanese.kanji.model.wadokux.util

object IntegerStringEncoder : PrimitiveStringEncoder<Int>("integerStr") {
    override fun makeInstance(deserialized: String) = deserialized.toInt()
}