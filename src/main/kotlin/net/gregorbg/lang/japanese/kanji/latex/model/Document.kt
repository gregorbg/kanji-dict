package net.gregorbg.lang.japanese.kanji.latex.model

import net.gregorbg.lang.japanese.kanji.latex.model.types.DocumentClass
import net.gregorbg.lang.japanese.kanji.latex.model.types.UsePackage
import net.gregorbg.lang.japanese.kanji.util.IndentMode
import net.gregorbg.lang.japanese.kanji.util.prependIndent
import net.gregorbg.lang.japanese.kanji.util.trimBlankLines

data class Document(
    val documentClass: EnumCommand<DocumentClass>,
    val packages: List<EnumCommand<UsePackage>>,
    val preamble: List<Command> = emptyList(),
    val body: String
) {
    val texSource: String
        get() = """
            ${documentClass.render()}
        
            ${packages.joinToString("\n") { it.render() }.prependIndent(3, IndentMode.SPACE).trimStart()}
            
            ${preamble.joinToString("\n") { it.render() }.prependIndent(3, IndentMode.SPACE).trimStart()}
        
            \begin{document}
                ${body.prependIndent(4, IndentMode.SPACE).trimStart()}
            \end{document}
        """.trimIndent().trimBlankLines()
}
