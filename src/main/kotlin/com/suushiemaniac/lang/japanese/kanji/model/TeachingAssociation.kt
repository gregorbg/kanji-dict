package com.suushiemaniac.lang.japanese.kanji.model

data class StructureAssociation(val type: AssociationType, val dictionary: Dictionary, val assoc: Int)

enum class AssociationType {
    PAGE, LESSON, JLPT_GRADE, ID_INDEX, LOOKUP_INDEX
}

data class Dictionary(val title: String, val isbn: String)
