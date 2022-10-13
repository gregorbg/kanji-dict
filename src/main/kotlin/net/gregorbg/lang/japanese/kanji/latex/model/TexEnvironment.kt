package net.gregorbg.lang.japanese.kanji.latex.model

data class TexEnvironment(
    override val name: String,
    override val options: String? = null,
    override val arguments: List<String> = emptyList(),
    override val body: String
): Environment
