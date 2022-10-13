package net.gregorbg.lang.japanese.kanji.source.twentytwo

import net.gregorbg.lang.japanese.kanji.model.Kanji
import net.gregorbg.lang.japanese.kanji.source.KanjiProgressionSource
import net.gregorbg.lang.japanese.kanji.source.workbook.parser.NumericalIndexParser
import net.gregorbg.lang.japanese.kanji.util.invert
import java.io.File

object TwentyTwoJitenSource : KanjiProgressionSource {
    private const val LESSONS_FILE_TAG = "jiten"

    var BASE_PATH = "/please/change/this/"

    private val lessonsContent by lazy { loadFile(LESSONS_FILE_TAG) }

    private val idParser by lazy { NumericalIndexParser(lessonsContent) }
    private val idData by lazy { idParser.getAssociations() }

    override fun lookupSymbol(kanji: Char): Kanji? {
        return null // TODO
    }

    override fun lookupIndex(index: Int): Kanji? {
        return idData.invert()[index]?.let { lookupSymbol(it.first()) }
    }

    override fun fetchAll(): List<Kanji> {
        return idData.keys.mapNotNull { lookupSymbol(it.first()) }
    }

    override fun getOrderingIndexFor(kanji: Kanji): Int {
        return idData[kanji.kanji.toString()] ?: -1
    }

    private fun loadFile(fileTag: String): String {
        val resourcePath = "${BASE_PATH.trimEnd('/')}/$fileTag.txt"
        return File(resourcePath).takeIf { it.exists() }?.reader()?.use { it.readText() }.orEmpty()
    }
}