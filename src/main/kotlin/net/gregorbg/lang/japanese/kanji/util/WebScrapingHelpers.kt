package net.gregorbg.lang.japanese.kanji.util

import net.gregorbg.lang.japanese.kanji.model.reading.token.*
import it.skrape.selects.DocElement
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode

fun DocElement.parseRuby() =
    html.parseRuby()

fun String.parseRuby() =
    Jsoup.parseBodyFragment(this).body().parseRubyRecursive()

private fun Node.parseRubyRecursive(): SymbolToken {
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
        // FIXME .flatMap { it.unwrap() }

    return ConvertedSymbolTokens(cleanedTokens)
}