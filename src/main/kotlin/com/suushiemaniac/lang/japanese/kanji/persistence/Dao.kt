package com.suushiemaniac.lang.japanese.kanji.persistence

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.sql.Column

class KanjiDao(id: EntityID<Int>) : IntEntity(id) {
    val symbol by KanjiTable.kanjiSymbol
    val strokeCount by KanjiTable.strokeCount
    val radical by RadicalDao referencedOn KanjiTable.radical

    companion object : IntEntityClass<KanjiDao>(KanjiTable)
}

class RadicalDao(id: EntityID<Int>) : IntEntity(id) {
    val symbol by RadicalTable.radicalSymbol
    val name by RadicalTable.name

    companion object : IntEntityClass<RadicalDao>(RadicalTable)
}

class DictionaryDao(id: EntityID<Int>) : IntEntity(id) {
    val title by DictionaryTable.title
    val authors by AuthorDao referrersOn AuthorTable.dictionary

    companion object : IntEntityClass<DictionaryDao>(DictionaryTable)
}

class AuthorDao(id: EntityID<Int>) : IntEntity(id) {
    val dictionary by DictionaryDao referencedOn AuthorTable.dictionary
    val name by AuthorTable.name

    companion object : IntEntityClass<AuthorDao>(AuthorTable)
}

class ReadingDao(id: EntityID<Int>) : IntEntity(id) {
    val kanji by KanjiDao referencedOn ReadingTable.kanjiId
    val kanaReading by ReadingTable.kanaReading
    val readingType by ReadingTable.readingType

    companion object : IntEntityClass<ReadingDao>(ReadingTable)
}

open class SampleDao(id: EntityID<Int>, table: SampleTable, childEntity: IntEntityClass<SampleTokenDao>, childTokenTable: SampleTokensTable, parentEntity: IntEntityClass<IntEntity>, parentReferenceColumn: Column<EntityID<Int>>): IntEntity(id) {
    val referenceObject by parentEntity referencedOn parentReferenceColumn
    val translation by table.translation
    val source by DictionaryDao referencedOn table.sourceDictionary
    val tokens by childEntity referrersOn childTokenTable.sampleId

    private companion object : IntEntityClass<SampleDao>(SampleTable("nope."))
}

class SampleWordDao(id: EntityID<Int>) : SampleDao(id, SampleWordTable, SampleWordTokenDao, SampleWordTokensTable, KanjiDao, SampleWordTable.kanjiId) {
    companion object : IntEntityClass<SampleWordDao>(SampleWordTable)
}

class SamplePhraseDao(id: EntityID<Int>) : SampleDao(id, SamplePhraseTable, SamplePhraseTokenDao, SamplePhraseTokensTable, SampleWordDao, SamplePhraseTable.sampleWordId) {
    companion object : IntEntityClass<SamplePhraseDao>(SamplePhraseTable)
}

open class SampleTokenDao(id: EntityID<Int>, table: SampleTokensTable, parentEntity: IntEntityClass<SampleDao>) : IntEntity(id) {
    val sample by parentEntity referencedOn table.sampleId
    val orderingIndex by table.orderingIndex
    val kanjiContent by table.kanjiContent
    val reading by table.reading
}

class SampleWordTokenDao(id: EntityID<Int>) : SampleTokenDao(id, SampleWordTokensTable, SampleWordDao) {
    companion object : IntEntityClass<SampleWordTokenDao>(SampleWordTokensTable)
}

class SamplePhraseTokenDao(id: EntityID<Int>) : SampleTokenDao(id, SamplePhraseTokensTable, SamplePhraseDao) {
    companion object : IntEntityClass<SamplePhraseTokenDao>(SamplePhraseTokensTable)
}

open class AssociationDao(id: EntityID<Int>, table: AssociationTable) : IntEntity(id) {
    val kanji by KanjiDao referencedOn table.kanjiId
    val dictionary by DictionaryDao referencedOn table.dictionaryId
    val association by table.association
}

class PageAssociationDao(id: EntityID<Int>) : AssociationDao(id, PageAssociationTable) {
    companion object : IntEntityClass<PageAssociationDao>(PageAssociationTable)
}

class LessonAssociationDao(id: EntityID<Int>) : AssociationDao(id, LessonAssociationTable) {
    companion object : IntEntityClass<LessonAssociationDao>(LessonAssociationTable)
}