package net.gregorbg.lang.japanese.kanji.latex.model.types

enum class DocumentClass(override val argName: String) : CommandType {
    ARTICLE_EU("scrartcl"),
    ARTICLE_US("article"),
    LTJ_ARTICLE("ltjarticle"),
    LTJ_ARTICLE_TATE("ltjtarticle"),
    ;

    override val commandName = "documentclass"
}