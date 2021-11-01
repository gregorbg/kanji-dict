package com.suushiemaniac.lang.japanese.kanji.model.wadokux.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

abstract class SingletonStringEncoder<T>(descriptionString: String) : KSerializer<T> {
    override val descriptor = PrimitiveSerialDescriptor(descriptionString, PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder) = makeInstance(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: T) = encoder.encodeString(encodeInstance(value))

    abstract fun encodeInstance(value: T): String
    abstract fun makeInstance(deserialized: String): T
}