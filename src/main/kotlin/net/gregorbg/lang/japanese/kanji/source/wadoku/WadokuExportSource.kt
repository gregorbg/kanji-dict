package net.gregorbg.lang.japanese.kanji.source.wadoku

import net.gregorbg.lang.japanese.kanji.model.Kanji
import net.gregorbg.lang.japanese.kanji.model.reading.token.TokenWithSurfaceForm
import net.gregorbg.lang.japanese.kanji.model.vocabulary.SampleSentence
import net.gregorbg.lang.japanese.kanji.model.vocabulary.Translation
import net.gregorbg.lang.japanese.kanji.model.vocabulary.VocabTagModifier
import net.gregorbg.lang.japanese.kanji.model.vocabulary.VocabularyItem
import net.gregorbg.lang.japanese.kanji.model.wadokux.WadokuExport
import net.gregorbg.lang.japanese.kanji.model.wadokux.WadokuExportEntry
import net.gregorbg.lang.japanese.kanji.model.wadokux.WadokuSense
import net.gregorbg.lang.japanese.kanji.model.wadokux.enumeration.SubEntryTypeEnum
import net.gregorbg.lang.japanese.kanji.source.KanjiSource
import net.gregorbg.lang.japanese.kanji.source.SampleSentenceSource
import net.gregorbg.lang.japanese.kanji.source.TranslationSource
import net.gregorbg.lang.japanese.kanji.source.VocabularySource
import net.gregorbg.lang.japanese.kanji.util.alignSymbolsWith
import net.gregorbg.lang.japanese.kanji.util.unlessEmpty
import kotlinx.serialization.decodeFromString
import nl.adaptivity.xmlutil.serialization.XML

class WadokuExportSource(exportXmlPath: String, val kanjiSource: KanjiSource) : VocabularySource, TranslationSource,
    SampleSentenceSource {
    private val export by lazy { XML_SERIAL.decodeFromString<WadokuExport>(exportXmlPath) }

    override fun getVocabularyItemsFor(kanji: Kanji): List<VocabularyItem> {
        return export.entries.filter { it.form.orthography.any { o -> kanji.kanji in o.orthography } }
            .filterNot { it.reference.any { r -> r.subEntryType in SENTENCE_TYPES } }
            .map { it.asVocabItem(kanjiSource) }
            .toList()
    }

    override fun getTranslationFor(token: TokenWithSurfaceForm): Translation? {
        val entry = export.entries.find { it.form.orthography.any { o -> o.orthography == token.surfaceForm } }
        val translations = entry?.sense?.flatMap { it.extrapolateTranslations() }

        return translations?.unlessEmpty()?.let {
            Translation(it.first(), it.drop(1))
        }
    }

    override fun getSampleSentencesFor(vocab: VocabularyItem): List<SampleSentence> {
        return export.entries.filter { it.form.orthography.any { o -> vocab.surfaceForm in o.orthography } }
            .filter { it.reference.all { r -> r.subEntryType in SENTENCE_TYPES } }
            .map { it.asSampleSentence() }
            .toList()
    }

    companion object {
        val XML_SERIAL = XML { repairNamespaces = true }

        val SENTENCE_TYPES =
            setOf(SubEntryTypeEnum.USAGE_EXAMPLE, SubEntryTypeEnum.IDIOMATIC, SubEntryTypeEnum.PHRASE, SubEntryTypeEnum.PROVERB)

        private fun WadokuExportEntry.asVocabItem(kanjiSource: KanjiSource): VocabularyItem {
            val surfaceForm = this.form.orthography.first().orthography
            val reading = this.form.reading?.hiragana.orEmpty()

            val combinedReading = surfaceForm.alignSymbolsWith(reading, kanjiSource)

            val modifiers = listOfNotNull(
                VocabTagModifier.VERB_SURU.takeIf { this.gramGrp?.meishi?.suru != null },
                VocabTagModifier.ADJECTIVE_NA.takeIf { this.gramGrp?.keiyoudoushi != null }
            )

            return VocabularyItem(combinedReading, modifiers)
        }

        private fun WadokuExportEntry.asSampleSentence(): SampleSentence {
            val surfaceForm = this.form.orthography.first().orthography

            return SampleSentence.parse(surfaceForm)
        }

        private fun WadokuSense.extrapolateTranslations(): List<String> {
            val tokenTypeCandidateNodes = translation
                .mapNotNull { it.tr }

            val tokenContents = tokenTypeCandidateNodes
                .flatMap { it.token }
                .map { it.token }

            val textContents = tokenTypeCandidateNodes
                .flatMap { it.text }
                .map { it.text }

            return tokenContents + textContents
        }
    }
}