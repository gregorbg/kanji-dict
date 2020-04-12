package com.suushiemaniac.lang.japanese.kanji.model.nhknews

import kotlinx.serialization.Serializable

@Serializable
data class NewsListRelationLink(
    val link: String,
    val title: String,
    val time: String,
    val datetime: String
)