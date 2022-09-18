package net.gregorbg.lang.japanese.kanji.anki

import net.gregorbg.lang.japanese.kanji.anki.AnkiExporter.JSON
import net.gregorbg.lang.japanese.kanji.anki.model.KanjiVocabPhrase
import net.gregorbg.lang.japanese.kanji.anki.model.KanjiVocabPhraseToken
import net.gregorbg.lang.japanese.kanji.anki.model.RubyFuriganaFormatter
import net.gregorbg.lang.japanese.kanji.model.vocabulary.VocabularyItem
import net.gregorbg.lang.japanese.kanji.model.reading.token.MorphologyToken
import net.gregorbg.lang.japanese.kanji.model.reading.token.TokenWithSurfaceForm
import net.gregorbg.lang.japanese.kanji.model.vocabulary.Translation
import net.gregorbg.lang.japanese.kanji.source.TranslationSource
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import net.gregorbg.lang.japanese.kanji.model.reading.token.level.SentenceLevelToken

data class KanjiVocabNote(
    val vocabItem: TokenWithSurfaceForm,
    val mainTranslation: String,
    val additionalTranslations: List<String>,
    val ankiPhrases: List<KanjiVocabPhrase>,
    val originalKanji: Char
) : AnkiDeckNote {
    override fun getCSVFacts(): List<String> {
        return listOf(
            vocabItem.surfaceForm,
            vocabItem.asFurigana(RubyFuriganaFormatter),
            mainTranslation,
            JSON.encodeToString(ListSerializer(String.serializer()), additionalTranslations),
            JSON.encodeToString(
                ListSerializer(ListSerializer(String.serializer())),
                ankiPhrases.map(KanjiVocabPhrase::getLiterals)
            ),
            JSON.encodeToString(
                ListSerializer(MapSerializer(String.serializer(), String.serializer())),
                ankiPhrases.map(KanjiVocabPhrase::getReadings)
            ),
            JSON.encodeToString(
                ListSerializer(
                    MapSerializer(
                        String.serializer(),
                        MapSerializer(String.serializer(), String.serializer())
                    )
                ),
                ankiPhrases.map(KanjiVocabPhrase::getTokenData)
            ),
            JSON.encodeToString(
                ListSerializer(MapSerializer(String.serializer(), String.serializer())),
                ankiPhrases.map(KanjiVocabPhrase::getAnnotations)
            )
        )
    }

    override fun getTags() =
        listOf(originalKanji.toString())

    companion object {
        const val SKIP_TOKEN_LITERAL = "_"
        val SKIP_TOKEN = MorphologyToken(SKIP_TOKEN_LITERAL, SKIP_TOKEN_LITERAL)

        fun from(
            item: VocabularyItem,
            translation: Translation,
            samplePhrases: List<SentenceLevelToken<MorphologyToken>>,
            originalKanji: Char,
            translationSource: TranslationSource
        ): KanjiVocabNote {
            val ankiPhrases = samplePhrases
                .map { it.toVocabTokens(translationSource) }
                .map { it.maskOriginalWordToken(item) }
                .map { KanjiVocabPhrase(it) }

            return KanjiVocabNote(
                item,
                translation.mainTranslation,
                translation.otherTranslations,
                ankiPhrases,
                originalKanji
            )
        }

        private fun SentenceLevelToken<MorphologyToken>.toVocabTokens(translationSource: TranslationSource): List<KanjiVocabPhraseToken> =
            this.tokens.map { KanjiVocabPhraseToken.from(it, translationSource) }

        private fun List<KanjiVocabPhraseToken>.maskOriginalWordToken(item: VocabularyItem): List<KanjiVocabPhraseToken> {
            return map {
                it.takeUnless { pt -> pt.surfaceForm == item.surfaceForm }
                    ?: KanjiVocabPhraseToken(SKIP_TOKEN)
            }
        }
    }
}