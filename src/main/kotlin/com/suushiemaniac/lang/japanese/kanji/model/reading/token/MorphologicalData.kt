package com.suushiemaniac.lang.japanese.kanji.model.reading.token

import com.atilika.kuromoji.ipadic.Token
import kotlinx.serialization.Serializable

@Serializable
data class MorphologicalData(
    val posLevels: List<String>,
    val baseForm: String,
    val pronunciation: String,
    val conjugationForm: String? = null,
    val conjugationType: String? = null
) {
    fun exportData(): Map<String, String> {
        val posMap = List(4) { i -> posLevels.getOrNull(i)?.let { "POS-${i + 1}" to it } }.filterNotNull().toMap()

        val conjMap = listOfNotNull(
            conjugationForm?.let { "CONJ-FORM" to it },
            conjugationType?.let { "CONJ-TYPE" to it }
        ).toMap()

        return posMap + mapOf(
            "BASE-FORM" to baseForm,
            "PRONOUNCE" to pronunciation
        ) + conjMap
    }

    companion object {
        private const val UNSPECIFIED_SKIP_TOKEN = "*"

        fun from(kuromojiToken: Token): MorphologicalData {
            val posLevels = listOfNotNull(
                kuromojiToken.partOfSpeechLevel1,
                kuromojiToken.partOfSpeechLevel2,
                kuromojiToken.partOfSpeechLevel3,
                kuromojiToken.partOfSpeechLevel4
            ).filterNot { it == UNSPECIFIED_SKIP_TOKEN }

            return MorphologicalData(
                posLevels,
                kuromojiToken.baseForm,
                kuromojiToken.pronunciation,
                kuromojiToken.conjugationForm.takeUnless { it == UNSPECIFIED_SKIP_TOKEN },
                kuromojiToken.conjugationType.takeUnless { it == UNSPECIFIED_SKIP_TOKEN }
            )
        }
    }
}
