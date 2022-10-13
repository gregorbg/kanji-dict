package net.gregorbg.lang.japanese.kanji.latex.model

interface Command {
    val name: String
    val options: String?
    val arguments: List<String>

    fun render(): String {
        val renderedOptions = options?.let { "[$it]" }.orEmpty()
        val renderedArgs = arguments.joinToString(separator = "") { "{$it}" }

        return "\\$name$renderedOptions$renderedArgs"
    }
}
