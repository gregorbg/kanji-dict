package com.suushiemaniac.lang.japanese.kanji.source.kanjium

import com.suushiemaniac.lang.japanese.kanji.model.Kanji
import com.suushiemaniac.lang.japanese.kanji.model.kanjium.Elements
import com.suushiemaniac.lang.japanese.kanji.model.kanjium.KanjiDictEntry
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.TokenWithSurfaceForm
import com.suushiemaniac.lang.japanese.kanji.model.vocabulary.Translation
import com.suushiemaniac.lang.japanese.kanji.source.KanjiElementsSource
import com.suushiemaniac.lang.japanese.kanji.source.kanjium.persistence.ElementsDao
import com.suushiemaniac.lang.japanese.kanji.source.kanjium.persistence.KanjiDictDao
import com.suushiemaniac.lang.japanese.kanji.source.kanjium.persistence.toModel
import com.suushiemaniac.lang.japanese.kanji.source.KanjiSource
import com.suushiemaniac.lang.japanese.kanji.source.TranslationSource
import com.suushiemaniac.lang.japanese.kanji.util.unlessEmpty
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

class KanjiumDatabaseSource(dbPath: String) : KanjiSource, TranslationSource, KanjiElementsSource {
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

    companion object {
        const val DRIVER = "org.sqlite.JDBC"

        private val KANJI_CACHE = mutableMapOf<Char, KanjiDictEntry>()
    }
}