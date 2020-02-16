package com.suushiemaniac.lang.japanese.kanji.persistence

import com.suushiemaniac.lang.japanese.kanji.model.AssociationType
import com.suushiemaniac.lang.japanese.kanji.model.ReadingType
import org.jetbrains.exposed.dao.IntIdTable

object KanjiTable : IntIdTable("kanji") {
    val kanjiSymbol = varchar("symbol", 4).uniqueIndex()
    val strokeCount = integer("stroke_count")
    val radical = reference("radical_id", RadicalTable).nullable()
}

object RadicalTable : IntIdTable("radicals") {
    val radicalSymbol = varchar("symbol", 4).uniqueIndex()
    val name = varchar("name", 256)
}

object DictionaryTable : IntIdTable("dictionary") {
    val title = varchar("title", 256)
    val isbn = varchar("isbn", 256)
}

object ReadingTable : IntIdTable("readings") {
    val kanjiId = reference("kanji_id", KanjiTable)
    val kanaReading = varchar("reading", 256)
    val readingType = enumerationByName("type", 32, ReadingType::class)
}

open class SampleTable(name: String, parentTable: IntIdTable) : IntIdTable(name) {
    val reading = varchar("reading", 1024)
    val translation = varchar("translation", 1024)
    val sourceDictionary = reference("source_id", DictionaryTable)
    val parentId = reference("parent_id", parentTable)
}

object SampleWordTable : SampleTable("sample_words", KanjiTable)
object SamplePhraseTable : SampleTable("sample_phrases", SampleWordTable)

object AssociationTable : IntIdTable("teaching_associations") {
    val kanjiId = reference("kanji_id", KanjiTable)
    val associationType = enumerationByName("type", 32, AssociationType::class)
    val dictionaryId = reference("dictionary_id", DictionaryTable)
    val association = integer("assoc_value")
}