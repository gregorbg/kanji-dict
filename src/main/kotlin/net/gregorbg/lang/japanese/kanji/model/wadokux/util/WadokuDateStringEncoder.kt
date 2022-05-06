package net.gregorbg.lang.japanese.kanji.model.wadokux.util

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object WadokuDateStringEncoder : SingletonStringEncoder<ZonedDateTime>("ZonedDateTime") {
    const val ENC_DATE_PATTERN = "E dd LLL yyyy KK:mm:ss a z"

    private val ENC_DATE_FORMAT = DateTimeFormatter.ofPattern(ENC_DATE_PATTERN, Locale.ENGLISH)

    override fun encodeInstance(value: ZonedDateTime) = value.format(ENC_DATE_FORMAT)
    override fun makeInstance(deserialized: String) = ZonedDateTime.parse(deserialized, ENC_DATE_FORMAT)
}