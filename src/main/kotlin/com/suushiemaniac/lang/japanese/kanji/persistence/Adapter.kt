package com.suushiemaniac.lang.japanese.kanji.persistence

import com.suushiemaniac.lang.japanese.kanji.model.*

fun KanjiDao.toModel() = KanjiCharacter(id.value, symbol.first(), strokeCount, radical?.toModel())
fun RadicalDao.toModel() = KanjiRadical(id.value, symbol.first(), name)
fun DictionaryDao.toModel() = Dictionary(id.value, title, isbn)
fun ReadingDao.toModel() = Reading(id.value, kanaReading, readingType)
fun SampleDao.toModel() = SampleContent(id.value, referenceObject.id.value, reading, translation, source.id.value)
fun AssociationDao.toModel() = StructureAssociation(kanji.id.value, type, dictionary.id.value, association)
