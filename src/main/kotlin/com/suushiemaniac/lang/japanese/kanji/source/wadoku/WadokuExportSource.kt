package com.suushiemaniac.lang.japanese.kanji.source.wadoku

import com.suushiemaniac.lang.japanese.kanji.model.Kanji
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.TokenWithSurfaceForm
import com.suushiemaniac.lang.japanese.kanji.model.vocabulary.Translation
import com.suushiemaniac.lang.japanese.kanji.model.vocabulary.VocabularyItem
import com.suushiemaniac.lang.japanese.kanji.source.KanjiSource
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

class WadokuExportSource(exportXmlPath: String, val kanjiSource: KanjiSource) : VocabularySource, TranslationSource {
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

    companion object {
        val DOC_BUILDER_FACTORY = DocumentBuilderFactory.newInstance()
            .apply { isNamespaceAware = true }

        val DOC_BUILDER = DOC_BUILDER_FACTORY.newDocumentBuilder()

        val JAXB_CONTEXT = JAXBContext.newInstance(JaxbEntry::class.java)
        val JAXB_UNMARSHALLER = JAXB_CONTEXT.createUnmarshaller()

        private fun JaxbEntry.asVocabItem(kanjiSource: KanjiSource): VocabularyItem {
            val surfaceForm = this.form.orth.first().value
            val reading = this.form.reading.hira

            val combinedReading = surfaceForm.alignSymbolsWith(reading, kanjiSource)

            // TODO modifiers (na, suru)
            return VocabularyItem(combinedReading)
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