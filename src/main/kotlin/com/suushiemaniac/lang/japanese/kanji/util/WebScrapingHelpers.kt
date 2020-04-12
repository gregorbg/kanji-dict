package com.suushiemaniac.lang.japanese.kanji.util

import com.suushiemaniac.lang.japanese.kanji.model.reading.token.*
import it.skrape.selects.DocElement
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode

fun DocElement.parseRuby() =
    html.parseRuby()

fun String.parseRuby() =
    Jsoup.parseBodyFragment(this).body().parseRubyRecursive()

fun Node.parseRubyRecursive(): TokenWithSurfaceForm {
    if (this is TextNode) {
        return KanaToken(text().orEmpty().trim())
    }

    if (this is Element && this.tagName() == "ruby") {
        val surface = ownText().orEmpty().trim()
        val reading = selectFirst("rt")?.ownText().orEmpty().trim()

        return if (surface.length == 1) {
            KanjiToken(surface.first(), reading)
        } else {
            MorphologyToken(surface, reading)
        }
    }

    val parsedTokens = childNodes().map { it.parseRubyRecursive() }

    return object : CompositeTokens<TokenWithSurfaceForm> {
        override val tokens = parsedTokens
    }
}