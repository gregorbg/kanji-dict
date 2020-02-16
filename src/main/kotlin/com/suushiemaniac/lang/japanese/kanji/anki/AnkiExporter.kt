package com.suushiemaniac.lang.japanese.kanji.anki

import com.suushiemaniac.lang.japanese.kanji.parser.*
import java.io.File

object AnkiExporter {
    fun makeCSVs(bookNumber: Int, idOffset: Int = 0) {
        val folder = File("/home/suushie_maniac/jvdocs/kanji-dict")

        val orderingsFile = SampleWord::class.java.getResource("/cram/15kanji_$bookNumber.txt")
        val readingsFile = SampleWord::class.java.getResource("/cram/15kanji_${bookNumber}_readings.txt")
        val samplesFile = SampleWord::class.java.getResource("/cram/15kanji_${bookNumber}_samples.txt")

        val orderings = orderingsFile.readText()
        val readings = readingsFile.readText()
        val samples = samplesFile.readText()

        val indexParser = NumericalIndexParser(orderings).getAssociations()
        val lessonsParser = LessonsParser(orderings).getAssociations()

        val onYomiParser = OnYomiParser(readings).getAssociations()
        val kunYomiParser = KunYomiParser(readings).getAssociations()

        val sampleWords = SampleWordParser(samples).getAssociations()

        val bookKanjis = indexParser.keys.toList()

        val csvSymbolsStructure = bookKanjis.map {
            listOf(
                it,
                onYomiParser.getValue(it).joinToString("<br/>"),
                kunYomiParser.getValue(it).joinToString("<br/>"),
                "TBD",
                "TBD",
                "L${lessonsParser.getValue(it)+1} id-${indexParser.getValue(it)+1+idOffset}"
            )
        }

        val csvSymbols = csvSymbolsStructure.joinToString("\n") { it.joinToString("\t") }
        val symbolCsv = File(folder, "symbols_$bookNumber.tsv")

        symbolCsv.writeText(csvSymbols)

        val csvVocabularyStructure = sampleWords.flatMap { (k, wd) ->
            wd.map {
                listOf(
                    it.kanji,
                    it.reading,
                    it.translation,
                    it.sentence,
                    "$k L${lessonsParser.getValue(k)+1}"
                )
            }
        }

        val csvVocabulary = csvVocabularyStructure.joinToString("\n") { it.joinToString("\t") }
        val vocabCsv = File(folder, "vocab_$bookNumber.tsv")

        vocabCsv.writeText(csvVocabulary)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        makeCSVs(3)
    }
}