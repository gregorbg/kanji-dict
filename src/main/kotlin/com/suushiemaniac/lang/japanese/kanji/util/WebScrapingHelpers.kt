package com.suushiemaniac.lang.japanese.kanji.util

import com.suushiemaniac.lang.japanese.kanji.model.reading.token.*
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.compose.CompositeReadingTokens
import it.skrape.selects.DocElement
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode

fun DocElement.parseRuby() =
    html.parseRuby()

fun String.parseRuby() =
    Jsoup.parseBodyFragment(this).body().parseRubyRecursive()

private fun Node.parseRubyRecursive(): ReadingToken {
    if (this is TextNode) {
        return KanaToken(text().orEmpty().trim())
    }

    if (this is Element && this.tagName() == "ruby") {
        val surface = childNode(0).parseRubyRecursive().surfaceForm
        val reading = selectFirst("rt")?.parseRubyRecursive()?.reading.orEmpty()

        return if (surface.length == 1) {
            KanjiToken(surface.first(), reading)
        } else {
            CompoundKanjiToken(surface, reading)
        }
    }

    val parsedTokens = childNodes()
        .filterNot { it is TextNode && it.isBlank }
        .map { it.parseRubyRecursive() }

    val cleanedTokens = parsedTokens
        .filter { it.surfaceForm.isNotBlank() }
        .flatMap { if (it is CompositeReadingTokens<*>) it.tokens else it.singletonList() }

    return ConvertedReadingTokens(cleanedTokens)
}