package com.suushiemaniac.lang.japanese.kanji.model.nhknews.easy

interface BaseNewsListItem {
    val newsId: String
    val newsPrearrangedTime: String
    val title: String
    val titleWithRuby: String
    val newsFileVer: Boolean
    val publicationStatus: Boolean
    val hasNewsWebImage: Boolean
    val hasNewsWebMovie: Boolean
    val hasNewsEasyImage: Boolean
    val hasNewsEasyMovie: Boolean
    val hasNewsEasyVoice: Boolean
    val newsWebImageUri: String
    val newsWebMovieUri: String
    val newsEasyImageUri: String
    val newsEasyMovieUri: String
    val newsEasyVoiceUri: String
    val priorityNumber: String
    val displayFlag: Boolean
}
