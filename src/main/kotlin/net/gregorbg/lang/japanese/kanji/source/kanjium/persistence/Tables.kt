package net.gregorbg.lang.japanese.kanji.source.kanjium.persistence

import org.jetbrains.exposed.dao.id.IdTable

object KanjiDictTable : IdTable<String>("kanjidict") {
    override val id = text("kanji").entityId()
    val radical = reference("radical",
        RadicalTable
    )
    val radvar = reference("radvar",
        RadVarTable
    ).nullable()
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
    val frequency = text("frequency").nullable()
    val meaning = text("meaning")
    val compactMeaning = text("compact_meaning").nullable()
}

abstract class RadicalBaseTable(name: String, idName: String) : IdTable<String>(name) {
    override val id = text(idName).entityId()
    val number = integer("number")
    val strokes = integer("strokes")
    val names = text("names")
    val meaning = text("meaning")
    val notes = text("notes").nullable()
}

object ElementsTable : IdTable<String>("elements") {
    override val id = reference("kanji",
        KanjiDictTable
    ) // TODO does this work?
    val strokes = integer("strokes")
    val grade = integer("grade")
    val idc = text("idc")
    val elements = text("elements")
    val extraElements = text("extra_elements").nullable()
    val kanjiParts = text("kanji_parts").nullable()
    val partOf = text("part_of").nullable()
    val compactMeaning = text("compact_meaning")
}

object RadicalTable : RadicalBaseTable("radicals", "radical")

object RadVarTable : RadicalBaseTable("radvars", "radvar") {
    val parentRef = reference("radical",
        RadicalTable
    )
}

object JukugoTable : IdTable<Int>("jukugo") {
    override val id = integer("id").entityId()
    val kanji = reference("kanji", KanjiDictTable)
    val jukugo = text("jukugo")
    val reading = text("reading")
    val meaning = text("meaning")
}

object SentencesTable : IdTable<Int>("sentences") {
    override val id = integer("id").entityId()
    val word = text("word")
    val japanese = text("japanese")
    val english = text("english")
}
