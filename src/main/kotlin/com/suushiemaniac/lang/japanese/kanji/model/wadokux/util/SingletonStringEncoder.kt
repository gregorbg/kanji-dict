package com.suushiemaniac.lang.japanese.kanji.model.wadokux.util

import kotlinx.serialization.*

abstract class SingletonStringEncoder<T>(val descriptionString: String) : KSerializer<T> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveDescriptor(descriptionString, PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder) = makeInstance(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: T) = encoder.encodeString(encodeInstance(value))

    abstract fun encodeInstance(value: T): String
    abstract fun makeInstance(deserialized: String): T
}