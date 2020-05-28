package com.suushiemaniac.lang.japanese.kanji.model.wadokux.util

object LongStringEncoder : PrimitiveStringEncoder<Long>("longStr") {
    override fun makeInstance(deserialized: String) = deserialized.toLong()
}