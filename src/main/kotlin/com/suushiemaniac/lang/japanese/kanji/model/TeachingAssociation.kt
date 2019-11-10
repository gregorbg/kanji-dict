package com.suushiemaniac.lang.japanese.kanji.model

data class StructureAssociation(val kanjiId: Int, val type: AssociationType, val dictionaryId: Int, val assoc: Int)

enum class AssociationType {
    PAGE, LESSON, JLPT_GRADE, ID_INDEX, LOOKUP_INDEX
}

data class Dictionary(val id: Int, val title: String, val isbn: String)
