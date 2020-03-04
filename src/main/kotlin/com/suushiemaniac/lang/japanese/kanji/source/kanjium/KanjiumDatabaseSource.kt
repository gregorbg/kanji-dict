package com.suushiemaniac.lang.japanese.kanji.source.kanjium

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
        Database.connect(dbPath, DRIVER)
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    }

    override fun get(kanji: Char): KanjiDictEntry {
        return KANJI_CACHE.getOrPut(kanji) { transaction { KanjiDictDao[kanji.toString()].toModel() } }
    }

    companion object {
        const val DRIVER = "org.sqlite.JDBC"

        private val KANJI_CACHE = mutableMapOf<Char, KanjiDictEntry>()
    }
}