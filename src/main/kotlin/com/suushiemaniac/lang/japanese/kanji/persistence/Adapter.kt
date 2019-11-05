package com.suushiemaniac.lang.japanese.kanji.persistence

import com.suushiemaniac.lang.japanese.kanji.model.*

fun KanjiDao.toModel() = KanjiCharacter(id.value, symbol.first(), strokeCount, radical.toModel())
fun RadicalDao.toModel() = KanjiRadical(id.value, symbol.first(), name)
fun DictionaryDao.toModel() = Dictionary(id.value, title, authors.map { it.toModel() })
fun AuthorDao.toModel() = Author(name)
fun ReadingDao.toModel() = Reading(id.value, kanaReading, readingType)
fun SampleDao.toModel() = SampleContent(id.value, referenceObject.id.value, tokens.sortedBy { it.orderingIndex }.map { it.toModel() }, translation, source.id.value)
fun SampleTokenDao.toModel() = ReadingToken(kanjiContent, reading)
fun AssociationDao.toModel() = StructureAssociation(kanji.id.value, dictionary.id.value, association)
