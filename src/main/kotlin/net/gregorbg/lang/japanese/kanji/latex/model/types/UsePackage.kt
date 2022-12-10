package net.gregorbg.lang.japanese.kanji.latex.model.types

enum class UsePackage(override val argName: String) : CommandType {
    GEOMETRY("geometry"),
    LUATEXJA("luatexja"),
    LUATEXJA_FONTSPEC("luatexja-fontspec"),
    LUATEXJA_RUBY("luatexja-ruby"),
    LUATEXJA_TATE_GEOMETRY("lltjp-geometry"),
    LUATEXJA_EXTENSIONS("lltjext"),
    ENUMITEM("enumitem"),
    XCOLOR("xcolor"),
    TABULARX("tabularx"),
    LTABLEX("ltablex"),
    HYPERREF("hyperref"),
    ;

    override val commandName = "usepackage"
}