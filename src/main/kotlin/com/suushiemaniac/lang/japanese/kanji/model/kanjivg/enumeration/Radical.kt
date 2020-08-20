package com.suushiemaniac.lang.japanese.kanji.model.kanjivg.enumeration

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Radical(val color: String) {
    @SerialName("general") GENERAL("#00ff00"), // green
    @SerialName("tradit") TRADITIONAL("#ffff00"), // yellow
    @SerialName("nelson") NELSON("#0000ff") // blue
}