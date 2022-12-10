package net.gregorbg.lang.japanese.kanji.latex

import net.gregorbg.lang.japanese.kanji.latex.model.Document
import net.gregorbg.lang.japanese.kanji.latex.model.EnumCommand
import net.gregorbg.lang.japanese.kanji.latex.model.TexCommand
import net.gregorbg.lang.japanese.kanji.latex.model.types.DocumentClass
import net.gregorbg.lang.japanese.kanji.latex.model.types.UsePackage

abstract class TabularKanjiTest : DocumentTest() {
    override fun renderDocument(body: String): Document {
        return Document(
            EnumCommand(DocumentClass.LTJ_ARTICLE),
            listOf(
                EnumCommand(UsePackage.LUATEXJA_FONTSPEC),
                EnumCommand(UsePackage.GEOMETRY),
                EnumCommand(UsePackage.LTABLEX),
                EnumCommand(UsePackage.HYPERREF),
            ),
            listOf(
                TexCommand("geometry", listOf("a4paper,margin=1cm")),
                TexCommand("newcolumntype", listOf("Y", ">{\\centering\\arraybackslash}X")),
                TexCommand("newcolumntype", listOf("s", ">{\\scriptsize}c")),
                TexCommand("renewcommand", listOf("\\arraystretch", "2.5")),
            ),
            body
        )
    }
}
