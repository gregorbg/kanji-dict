package com.suushiemaniac.lang.japanese.kanji.persistence

import com.suushiemaniac.lang.japanese.kanji.model.ReadingType
import org.jetbrains.exposed.dao.IntIdTable

object KanjiTable : IntIdTable("kanji") {
    val kanjiSymbol = varchar("symbol", 4).uniqueIndex()
    val strokeCount = integer("stroke_count")
    val radical = reference("radical_id", RadicalTable)
}

object RadicalTable : IntIdTable("radicals") {
    val radicalSymbol = varchar("symbol", 4).uniqueIndex()
    val name = varchar("name", 256)
}

object DictionaryTable : IntIdTable("dictionary") {
    val title = varchar("title", 256)
}

object AuthorTable : IntIdTable("dictionary_authors") {
    val dictionary = reference("dictionary_id", DictionaryTable)
    val name = varchar("name", 256)
}

object ReadingTable : IntIdTable("readings") {
    val kanjiId = reference("kanji_id", KanjiTable)
    val kanaReading = varchar("reading", 256)
    val readingType = enumeration("type", ReadingType::class)
}

open class SampleTable(name: String) : IntIdTable(name) {
    val translation = varchar("translation", 1024)
    val sourceDictionary = reference("source_id", DictionaryTable)
}

object SampleWordTable : SampleTable("sample_words") {
    val kanjiId = reference("kanji_id", KanjiTable)
}

object SamplePhraseTable : SampleTable("sample_phrases") {
    val sampleWordId = reference("sample_word_id", SampleWordTable)
}

open class SampleTokensTable(name: String, foreign: IntIdTable) : IntIdTable(name) {
    val sampleId = reference("sample_id", foreign)
    val orderingIndex = integer("ordering_index")
    val kanjiContent = varchar("kanji_content", 256)
    val reading = varchar("reading", 256)
}

object SampleWordTokensTable : SampleTokensTable("sample_word_tokens", SampleWordTable)
object SamplePhraseTokensTable : SampleTokensTable("sample_phrase_tokens", SamplePhraseTable)

open class AssociationTable(name: String, associationName: String) : IntIdTable(name) {
    val kanjiId = integer("kanji_id").references(KanjiTable.id)
    val dictionaryId = integer("dictionary_id").references(DictionaryTable.id)
    val association = integer(associationName)
}

object PageAssociationTable : AssociationTable("associations_page", "page")
object LessonAssociationTable : AssociationTable("associations_lesson", "lesson")