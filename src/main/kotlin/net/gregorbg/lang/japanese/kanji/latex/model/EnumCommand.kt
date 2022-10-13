package net.gregorbg.lang.japanese.kanji.latex.model

import net.gregorbg.lang.japanese.kanji.latex.model.types.CommandType

data class EnumCommand<out T : CommandType>(
    val enumArgument: T,
    override val options: String? = null
): Command {
    override val name = enumArgument.commandName
    override val arguments = listOf(enumArgument.argName)
}
