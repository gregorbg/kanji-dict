package com.suushiemaniac.lang.japanese.kanji.persistence

import org.jetbrains.exposed.dao.*

class KanjiDao(id: EntityID<Int>) : IntEntity(id) {
    val symbol by KanjiTable.kanjiSymbol
    val strokeCount by KanjiTable.strokeCount
    val radical by RadicalDao optionalReferencedOn KanjiTable.radical

    companion object : IntEntityClass<KanjiDao>(KanjiTable)
}

class RadicalDao(id: EntityID<Int>) : IntEntity(id) {
    val symbol by RadicalTable.radicalSymbol
    val name by RadicalTable.name

    companion object : IntEntityClass<RadicalDao>(RadicalTable)
}

class DictionaryDao(id: EntityID<Int>) : IntEntity(id) {
    val title by DictionaryTable.title
    val isbn by DictionaryTable.isbn

    companion object : IntEntityClass<DictionaryDao>(DictionaryTable)
}

class ReadingDao(id: EntityID<Int>) : IntEntity(id) {
    val kanji by KanjiDao referencedOn ReadingTable.kanjiId
    val kanaReading by ReadingTable.kanaReading
    val readingType by ReadingTable.readingType

    companion object : IntEntityClass<ReadingDao>(ReadingTable)
}

open class SampleDao(id: EntityID<Int>, table: SampleTable, parentEntity: IntEntityClass<IntEntity>) : IntEntity(id) {
    val referenceObject by parentEntity referencedOn table.parentId
    val translation by table.translation
    val source by DictionaryDao referencedOn table.sourceDictionary
    val reading by table.reading
}

class SampleWordDao(id: EntityID<Int>) : SampleDao(id, SampleWordTable, KanjiDao) {
    companion object : IntEntityClass<SampleWordDao>(SampleWordTable)
}

class SamplePhraseDao(id: EntityID<Int>) : SampleDao(id, SamplePhraseTable, SampleWordDao) {
    companion object : IntEntityClass<SamplePhraseDao>(SamplePhraseTable)
}

open class AssociationDao(id: EntityID<Int>) : IntEntity(id) {
    val kanji by KanjiDao referencedOn AssociationTable.kanjiId
    val type by AssociationTable.associationType
    val dictionary by DictionaryDao referencedOn AssociationTable.dictionaryId
    val association by AssociationTable.association
}
