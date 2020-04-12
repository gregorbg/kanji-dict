package com.suushiemaniac.lang.japanese.kanji.model.nhknews

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChannelNewsListItem(
    val id: String,
    val title: String,
    val pubDate: String,
    @SerialName("cate") val category: String,
    @SerialName("cate_group") val categoryGroups: List<String>,
    val link: String,
    val imgPath: String,
    val iconPath: String,
    val videoPath: String,
    val videoDuration: String,
    @SerialName("relationNews") val relatedNews: List<NewsListRelationArticle> = emptyList(),
    @SerialName("relationLink") val relatedLinks: List<NewsListRelationLink> = emptyList(),
    @SerialName("word") val relatedTags: List<NewsListTag> = emptyList()
)