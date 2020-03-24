package com.suushiemaniac.lang.japanese.kanji.model.kanjium.enumeration

import com.suushiemaniac.lang.japanese.kanji.model.reading.type.KunYomiAnnotationMode
import com.suushiemaniac.lang.japanese.kanji.util.words

enum class KanjiMinistryList {
    KYOUIKU,
    JOUYOU,
    JINMEIYOU,
    HYOUGAI,
    KYUUJITAI;

    companion object {
        val KANJIUM_TITLE_MAPPINGS = mapOf(
            "Kyōiku-Jōyō" to KYOUIKU,
            "Jōyō" to JOUYOU,
            "Jinmeiyō" to JINMEIYOU,
            "Hyōgaiji" to HYOUGAI,
            "Kyūjitai-Hyōgaiji" to KYUUJITAI
        )

        val SCHOOL_GRADE_LISTS = listOf(KYOUIKU, JOUYOU)
        val SCHOOL_GRADE_HIGHSCHOOL = "high school"

        fun parseList(gradeDescription: String): KanjiMinistryList {
            val listTitle = gradeDescription.words.first()
            return KANJIUM_TITLE_MAPPINGS.getValue(listTitle)
        }

        fun parseEducationGrade(gradeDescription: String): Int? {
            val list = parseList(gradeDescription)

            if (list in SCHOOL_GRADE_LISTS) {
                val fakeParse = KunYomiAnnotationMode.BracketKunYomiParser.parse(gradeDescription)
                val descriptionText = fakeParse.okurigana.orEmpty()

                if (descriptionText == SCHOOL_GRADE_HIGHSCHOOL) {
                    return 10
                }

                val levelNumerator = descriptionText.words.first()

                val schoolBias = if (list == JOUYOU) 6 else 0
                val parsedNum = levelNumerator.takeWhile { it.isDigit() }.toInt()

                return parsedNum + schoolBias
            }

            return null
        }
    }
}
