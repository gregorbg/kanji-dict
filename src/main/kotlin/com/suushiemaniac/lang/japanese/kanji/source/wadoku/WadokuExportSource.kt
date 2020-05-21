package com.suushiemaniac.lang.japanese.kanji.source.wadoku

import com.suushiemaniac.lang.japanese.kanji.model.Kanji
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.TokenWithSurfaceForm
import com.suushiemaniac.lang.japanese.kanji.model.vocabulary.SampleSentence
import com.suushiemaniac.lang.japanese.kanji.model.vocabulary.Translation
import com.suushiemaniac.lang.japanese.kanji.model.vocabulary.VocabularyItem
import com.suushiemaniac.lang.japanese.kanji.source.KanjiSource
import com.suushiemaniac.lang.japanese.kanji.source.SampleSentenceSource
import com.suushiemaniac.lang.japanese.kanji.source.TranslationSource
import com.suushiemaniac.lang.japanese.kanji.source.VocabularySource
import com.suushiemaniac.lang.japanese.kanji.util.alignSymbolsWith
import com.suushiemaniac.lang.japanese.kanji.util.unlessEmpty
import de.wadoku.xml.entry.*
import org.w3c.dom.Node
import java.io.File
import java.io.Serializable
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBElement
import javax.xml.parsers.DocumentBuilderFactory

class WadokuExportSource(exportXmlPath: String, val kanjiSource: KanjiSource) : VocabularySource, TranslationSource,
    SampleSentenceSource {
    private val document = DOC_BUILDER.parse(File(exportXmlPath))

    private val entryNodes = document.getElementsByTagName("entry")
    private val numEntries = entryNodes.length

    private val nodeSequence: Sequence<Node>
        get() = (0 until numEntries).asSequence()
            .map { entryNodes.item(it) }

    private val entrySequence: Sequence<JaxbEntry>
        get() = nodeSequence.mapNotNull { JAXB_UNMARSHALLER.unmarshal(it) as? JaxbEntry }

    override fun getVocabularyItemsFor(kanji: Kanji): List<VocabularyItem> {
        return entrySequence.filter { it.form.orth.any { o -> kanji.kanji in o.value } }
            .filterNot { it.ref.any { r -> r.subentrytype in SENTENCE_TYPES } }
            .map { it.asVocabItem(kanjiSource) }
            .toList()
    }

    override fun getTranslationFor(token: TokenWithSurfaceForm): Translation? {
        val entry = entrySequence.find { it.form.orth.any { o -> o.value == token.surfaceForm } }
        val translations = entry?.sense?.flatMap { it.transAndBracketAndDef.extrapolateTranslations() }

        return translations?.unlessEmpty()?.let {
            Translation(it.first(), it.drop(1))
        }
    }

    override fun getSampleSentencesFor(vocab: VocabularyItem): List<SampleSentence> {
        return entrySequence.filter { it.form.orth.any { o -> vocab.surfaceForm in o.value } }
            .filter { it.ref.all { r -> r.subentrytype in SENTENCE_TYPES } }
            .map { it.asSampleSentence() }
            .toList()
    }

    companion object {
        val DOC_BUILDER_FACTORY = DocumentBuilderFactory.newInstance()
            .apply { isNamespaceAware = true }

        val DOC_BUILDER = DOC_BUILDER_FACTORY.newDocumentBuilder()

        val JAXB_CONTEXT = JAXBContext.newInstance(JaxbEntry::class.java)
        val JAXB_UNMARSHALLER = JAXB_CONTEXT.createUnmarshaller()

        val SENTENCE_TYPES =
            setOf(SubEntryTypeEnum.VW_BSP, SubEntryTypeEnum.W_IDIOM, SubEntryTypeEnum.X_SATZ, SubEntryTypeEnum.Z_SPR_W)

        private fun JaxbEntry.asVocabItem(kanjiSource: KanjiSource): VocabularyItem {
            val surfaceForm = this.form.orth.first().value
            val reading = this.form.reading.hira

            val combinedReading = surfaceForm.alignSymbolsWith(reading, kanjiSource)

            // TODO modifiers (na, suru)
            return VocabularyItem(combinedReading)
        }

        private fun JaxbEntry.asSampleSentence(): SampleSentence {
            val surfaceForm = this.form.orth.first().value

            return SampleSentence.parse(surfaceForm)
        }

        private fun List<JAXBElement<out Serializable>>.extrapolateTranslations(): List<String> {
            val tokenTypeCandidateNodes = map { it.value }.filterIsInstance<TransType>()
                .flatMap { it.usgAndTrAndDef }.filterIsInstance<TrComplexType>()
                .flatMap { it.textAndTokenAndDef }.map { it.value }

            val tokenContents = tokenTypeCandidateNodes.filterIsInstance<TokenType>()
                .mapNotNull { it.content }

            val textContents = tokenTypeCandidateNodes.filterIsInstance<TextType>()
                .mapNotNull { it.value }

            return tokenContents + textContents
        }
    }
}