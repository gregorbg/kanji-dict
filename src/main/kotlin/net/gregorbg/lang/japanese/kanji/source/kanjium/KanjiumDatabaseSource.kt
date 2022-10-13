package net.gregorbg.lang.japanese.kanji.source.kanjium

import net.gregorbg.lang.japanese.kanji.model.Kanji
import net.gregorbg.lang.japanese.kanji.model.kanjium.Elements
import net.gregorbg.lang.japanese.kanji.model.kanjium.KanjiDictEntry
import net.gregorbg.lang.japanese.kanji.model.reading.token.MorphologyToken
import net.gregorbg.lang.japanese.kanji.model.reading.token.TokenWithSurfaceForm
import net.gregorbg.lang.japanese.kanji.model.vocabulary.SampleSentence
import net.gregorbg.lang.japanese.kanji.model.vocabulary.Translation
import net.gregorbg.lang.japanese.kanji.model.vocabulary.VocabTagModifier
import net.gregorbg.lang.japanese.kanji.model.vocabulary.VocabularyItem
import net.gregorbg.lang.japanese.kanji.source.*
import net.gregorbg.lang.japanese.kanji.source.kanjium.persistence.*
import net.gregorbg.lang.japanese.kanji.util.alignSymbolsWith
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

class KanjiumDatabaseSource(dbPath: String) : KanjiSource, TranslationSource, KanjiElementsSource, VocabularySource, SampleSentenceSource<MorphologyToken> {
    init {
        Database.connect("jdbc:sqlite:$dbPath", DRIVER)
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    }

    override fun lookupSymbol(kanji: Char): KanjiDictEntry? {
        val lookup = KANJI_CACHE[kanji] ?: transaction { KanjiDictDao.findById(kanji.toString())?.toModel() }
        return lookup?.also { KANJI_CACHE[kanji] = it }
    }

    override fun getElementsFor(kanji: Kanji): Elements? {
        return transaction { ElementsDao.findById(kanji.kanji.toString())?.toModel() }
    }

    override fun getTranslationFor(token: TokenWithSurfaceForm): Translation? {
        return token.surfaceForm.singleOrNull()?.let {
            val registry = this.lookupSymbol(it)
                ?: return@let null

            val compact = registry.compactMeaning
            val full = registry.meaning - compact

            Translation(compact.joinToString(), full)
        }
    }

    override fun getVocabularyItemsFor(kanji: Kanji): List<VocabularyItem> {
        return transaction {
            JukugoDao.find { JukugoTable.kanji eq kanji.kanji.toString() }.map {
                val alignedReading = it.jukugo.alignSymbolsWith(it.reading, this@KanjiumDatabaseSource)
                val modifiers = VocabTagModifier.values().filter { m -> m.kana in it.meaning }

                VocabularyItem(alignedReading, modifiers)
            }
        }
    }

    override fun getSampleSentencesFor(vocab: VocabularyItem): List<SampleSentence<MorphologyToken>> {
        return transaction {
            SentencesDao.find { SentencesTable.word eq vocab.surfaceForm }.map {
                SampleSentence.parseWithMorphology(it.japanese)
            }
        }
    }

    companion object {
        const val DRIVER = "org.sqlite.JDBC"

        private val KANJI_CACHE = mutableMapOf<Char, KanjiDictEntry>()
    }
}