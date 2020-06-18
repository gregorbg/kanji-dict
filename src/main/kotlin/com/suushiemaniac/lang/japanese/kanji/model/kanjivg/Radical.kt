package com.suushiemaniac.lang.japanese.kanji.model.kanjivg

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Radical(val color: String) {
    @SerialName("general") GENERAL("#ff0000"), // red
    @SerialName("nelson") NELSON("#00ff00"), // green
    @SerialName("tradit") TRADITIONAL("#0000ff") // blue
}