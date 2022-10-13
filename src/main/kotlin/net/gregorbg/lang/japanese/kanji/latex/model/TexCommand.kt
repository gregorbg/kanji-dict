package net.gregorbg.lang.japanese.kanji.latex.model

data class TexCommand(
    override val name: String,
    override val arguments: List<String>,
    override val options: String? = null
) : Command
