package com.suushiemaniac.lang.japanese.kanji.model

data class Dictionary(val id: Int, val title: String, val authors: List<Author>)

data class Author(val name: String)