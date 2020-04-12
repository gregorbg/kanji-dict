package com.suushiemaniac.lang.japanese.kanji.model.nhknews

import kotlinx.serialization.Serializable

@Serializable
data class NewsListRelationArticle(
    val link: String,
    val title: String
)