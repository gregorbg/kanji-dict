package com.suushiemaniac.lang.japanese.kanji.persistence

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID

class KanjiDictDao(id: EntityID<String>) : Entity<String>(id) {
    val radical by RadicalDao referencedOn KanjiDictTable.radical
    val radVar by RadVarDao optionalReferencedOn KanjiDictTable.radvar
    val phonetic by KanjiDictTable.phonetic
    val idc by KanjiDictTable.idc
    val type by KanjiDictTable.type
    val regOn by KanjiDictTable.regOn
    val regKun by KanjiDictTable.regKun
    val onyomi by KanjiDictTable.onyomi
    val kunyomi by KanjiDictTable.kunyomi
    val nanori by KanjiDictTable.nanori
    val strokes by KanjiDictTable.strokes
    val grade by KanjiDictTable.grade
    val jlpt by KanjiDictTable.jlpt
    val kanken by KanjiDictTable.kanken
    val frequency by KanjiDictTable.frequency
    val meaning by KanjiDictTable.meaning
    val compactMeaning by KanjiDictTable.compactMeaning

    companion object : EntityClass<String, KanjiDictDao>(KanjiDictTable)
}

abstract class RadicalBaseDao(id: EntityID<String>, table: RadicalBaseTable) : Entity<String>(id) {
    val number by table.number
    val strokes by table.strokes
    val names by table.names
    val meaning by table.meaning
    val notes by table.notes
}

class RadicalDao(id: EntityID<String>) : RadicalBaseDao(id, RadicalTable) {
    companion object : EntityClass<String, RadicalBaseDao>(RadicalTable)
}

class RadVarDao(id: EntityID<String>) : RadicalBaseDao(id, RadVarTable) {
    val parentRef by RadicalDao referencedOn RadVarTable.parentRef

    companion object : EntityClass<String, RadicalBaseDao>(RadVarTable)
}

class ElementsDao(id: EntityID<String>) : Entity<String>(id) {
    val kanji by KanjiDictDao referencedOn ElementsTable.id
    val strokes by ElementsTable.strokes
    val grade by ElementsTable.grade
    val idc by ElementsTable.idc
    val elements by ElementsTable.elements
    val extraElements by ElementsTable.extraElements
    val kanjiParts by ElementsTable.kanjiParts
    val partOf by ElementsTable.partOf
    val compactMeaning by ElementsTable.compactMeaning

    companion object : EntityClass<String, ElementsDao>(ElementsTable)
}
