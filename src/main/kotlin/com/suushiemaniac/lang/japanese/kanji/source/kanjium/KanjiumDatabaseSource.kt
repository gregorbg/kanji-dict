package com.suushiemaniac.lang.japanese.kanji.source.kanjium

import com.suushiemaniac.lang.japanese.kanji.model.Kanji
import com.suushiemaniac.lang.japanese.kanji.model.kanjium.Elements
import com.suushiemaniac.lang.japanese.kanji.model.kanjium.KanjiDictEntry
import com.suushiemaniac.lang.japanese.kanji.source.kanjium.persistence.ElementsDao
import com.suushiemaniac.lang.japanese.kanji.source.kanjium.persistence.KanjiDictDao
import com.suushiemaniac.lang.japanese.kanji.source.kanjium.persistence.toModel
import com.suushiemaniac.lang.japanese.kanji.source.KanjiSource
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

class KanjiumDatabaseSource(dbPath: String) : KanjiSource {
    init {
        Database.connect("jdbc:sqlite:$dbPath", DRIVER)
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    }

    override fun lookupSymbol(kanji: Char): KanjiDictEntry? {
        val lookup = KANJI_CACHE[kanji] ?: transaction { KanjiDictDao.findById(kanji.toString())?.toModel() }
        return lookup?.also { KANJI_CACHE[kanji] = it }
    }

    fun getElementsFor(kanji: Kanji): Elements? {
        return transaction { ElementsDao.findById(kanji.kanji.toString())?.toModel() }
    }

    companion object {
        const val DRIVER = "org.sqlite.JDBC"

        private val KANJI_CACHE = mutableMapOf<Char, KanjiDictEntry>()
    }
}