package com.suushiemaniac.lang.japanese.kanji.source.kanjium

import com.suushiemaniac.lang.japanese.kanji.model.Kanji
import com.suushiemaniac.lang.japanese.kanji.model.kanjium.KanjiDictEntry
import com.suushiemaniac.lang.japanese.kanji.persistence.KanjiDictDao
import com.suushiemaniac.lang.japanese.kanji.persistence.toModel
import com.suushiemaniac.lang.japanese.kanji.source.KanjiSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

class KanjiumDatabaseSource(dbPath: String) : KanjiSource {
    init {
        Database.connect("jdbc:sqlite:$dbPath", DRIVER)
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    }

    override fun lookupSymbol(kanji: Char): KanjiDictEntry {
        return KANJI_CACHE.getOrPut(kanji) { transaction { KanjiDictDao[kanji.toString()].toModel() } }
    }

    override fun fetchAll(): List<Kanji> {
        val fullFetch = transaction { KanjiDictDao.all() }

        return fullFetch.map {
            it.toModel().also { m -> KANJI_CACHE[m.kanji] = m }
        }
    }

    companion object {
        const val DRIVER = "org.sqlite.JDBC"

        private val KANJI_CACHE = mutableMapOf<Char, KanjiDictEntry>()
    }
}