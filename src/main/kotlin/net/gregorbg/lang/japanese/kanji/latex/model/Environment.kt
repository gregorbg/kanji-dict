package net.gregorbg.lang.japanese.kanji.latex.model

interface Environment : Command {
    val body: String

    override fun render(): String {
        val renderedOptions = options?.let { "[$it]" }.orEmpty()
        val renderedArgs = arguments.joinToString(separator = "") { "{$it}" }

        return """
            \begin{$name}$renderedArgs$renderedOptions
                $body
            \end{$name}
        """.trimIndent()
    }
}
