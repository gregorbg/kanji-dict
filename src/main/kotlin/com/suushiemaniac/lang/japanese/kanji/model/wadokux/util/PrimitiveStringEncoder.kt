package com.suushiemaniac.lang.japanese.kanji.model.wadokux.util

abstract class PrimitiveStringEncoder<T>(descriptionString: String) : SingletonStringEncoder<T>(descriptionString) {
    override fun encodeInstance(value: T) = value.toString()
}