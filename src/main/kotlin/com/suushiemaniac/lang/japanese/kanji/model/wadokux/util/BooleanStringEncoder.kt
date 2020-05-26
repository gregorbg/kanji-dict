package com.suushiemaniac.lang.japanese.kanji.model.wadokux.util

object BooleanStringEncoder : PrimitiveStringEncoder<Boolean>("booleanStr") {
    override fun makeInstance(deserialized: String) = deserialized.toBoolean()
}