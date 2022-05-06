package net.gregorbg.lang.japanese.kanji.model.nhknews.easy

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EasyNewsListItem(
    @SerialName("news_priority_number") override val priorityNumber: String,
    @SerialName("news_id") override val newsId: String,
    @SerialName("news_prearranged_time") override val newsPrearrangedTime: String,
    override val title: String,
    @SerialName("title_with_ruby") override val titleWithRuby: String,
    @SerialName("news_file_ver") override val newsFileVer: Boolean,
    @SerialName("news_publication_status") override val publicationStatus: Boolean,
    @SerialName("has_news_web_image") override val hasNewsWebImage: Boolean,
    @SerialName("has_news_web_movie") override val hasNewsWebMovie: Boolean,
    @SerialName("has_news_easy_image") override val hasNewsEasyImage: Boolean,
    @SerialName("has_news_easy_movie") override val hasNewsEasyMovie: Boolean,
    @SerialName("has_news_easy_voice") override val hasNewsEasyVoice: Boolean,
    @SerialName("news_web_image_uri") override val newsWebImageUri: String,
    @SerialName("news_web_movie_uri") override val newsWebMovieUri: String,
    @SerialName("news_easy_image_uri") override val newsEasyImageUri: String,
    @SerialName("news_easy_movie_uri") override val newsEasyMovieUri: String,
    @SerialName("news_easy_voice_uri") override val newsEasyVoiceUri: String,
    @SerialName("news_display_flag") override val displayFlag: Boolean,
    @SerialName("news_creation_time") val newsCreationTime: String,
    @SerialName("news_preview_time") val newsPreviewTime: String,
    @SerialName("news_publication_time") val newsPublicationTime: String,
    @SerialName("news_web_url") val newsWebUrl: String
) : BaseNewsListItem
