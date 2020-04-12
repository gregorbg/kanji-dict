package com.suushiemaniac.lang.japanese.kanji.model.nhknews

import kotlinx.serialization.Serializable

@Serializable
data class NewsListTag(
    val link: String,
    val title: String
)