package com.suushiemaniac.lang.japanese.kanji.persistence

import org.jetbrains.exposed.dao.id.IdTable

object KanjiDictTable : IdTable<String>("kanjidict") {
    override val id = text("kanji").entityId()
    val radical = reference("radical", RadicalBaseTable)
    val radvar = reference("radvar", RadVarTable).nullable()
    val phonetic = text("phonetic").nullable()
    val idc = text("idc")
    val type = text("type").nullable()
    val regOn = text("reg_on").nullable()
    val regKun = text("reg_kun").nullable()
    val onyomi = text("onyomi").nullable()
    val kunyomi = text("kunyomi").nullable()
    val nanori = text("nanori").nullable()
    val strokes = integer("strokes")
    val grade = text("grade")
    val jlpt = text("jlpt").nullable()
    val kanken = text("kanken").nullable()
    val frequency = integer("frequency").nullable()
    val meaning = text("meaning")
    val compactMeaning = text("compact_meaning").nullable()
}

open class RadicalTable(name: String, idName: String, refName: String, refTable: IdTable<String>) :
    IdTable<String>(name) {
    override val id = text(idName).entityId()
    val parentRef = reference(refName, refTable).nullable() // FIXME dynamic nullability?
    val number = integer("number")
    val strokes = integer("strokes")
    val names = text("names")
    val meaning = text("meaning")
    val notes = text("notes").nullable()
}

object ElementsTable : IdTable<String>("elements") {
    override val id = reference("kanji", KanjiDictTable) // TODO does this work?
    val strokes = integer("strokes")
    val grade = integer("grade")
    val idc = text("idc")
    val elements = text("elements")
    val extraElements = text("extra_elements").nullable()
    val kanjiParts = text("kanji_parts").nullable()
    val partOf = text("part_of").nullable()
    val compactMeaning = text("compact_meaning")
}

object RadicalBaseTable : RadicalTable("radicals", "radical", "radvar", RadVarTable)
object RadVarTable : RadicalTable("radvars", "radvar", "radical", RadicalBaseTable)
