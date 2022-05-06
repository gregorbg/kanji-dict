package net.gregorbg.lang.japanese.kanji.model.nhknews

import kotlinx.serialization.Serializable

@Serializable
data class ChannelNewsList(
    val lastBuildDate: String,
    val item: List<ChannelNewsListItem>,
    val hasNext: Boolean = false
)