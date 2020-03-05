package com.suushiemaniac.lang.japanese.kanji.source.jisho

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive

@Serializable
data class JishoAttributions(val jmdict: JsonPrimitive, val jmnedict: JsonPrimitive, val dbpedia: JsonPrimitive)