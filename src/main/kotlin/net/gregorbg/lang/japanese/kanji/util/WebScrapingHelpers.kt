package net.gregorbg.lang.japanese.kanji.util

import net.gregorbg.lang.japanese.kanji.model.reading.token.*
import it.skrape.selects.DocElement
import net.gregorbg.lang.japanese.kanji.model.reading.token.level.WordLevelToken
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode

fun DocElement.parseRuby() =
    html.parseRuby()

fun String.parseRuby() =
    Jsoup.parseBodyFragment(this).body().parseRubyRecursive()

private fun Node.parseRubyRecursive(): List<WordLevelToken> {
    if (this is TextNode) {
        return CompoundKanaToken(text().orEmpty().trim()).singletonList()
    }

    if (this is Element && this.tagName() == "ruby") {
        val surface = child(0).text().orEmpty().trim()
        val reading = selectFirst("rt")?.text().orEmpty().trim()

        return CompoundKanjiToken(surface, reading).singletonList()
    }

    val parsedTokens = childNodes()
        .filterNot { it is TextNode && it.isBlank }
        .flatMap { it.parseRubyRecursive() }

    return parsedTokens
        .filter { it.surfaceForm.isNotBlank() }
}