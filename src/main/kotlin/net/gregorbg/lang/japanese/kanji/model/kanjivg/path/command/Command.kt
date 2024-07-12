package net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command

enum class Command(val svgCommand: Char) {
    MOVE_TO('M'),
    LINE('L'),
    HORIZONTAL_LINE('H'),
    VERTICAL_LINE('V'),
    CLOSE_PATH('Z'),
    BEZIER_CURVE('C'),
    SYMMETRIC_BEZIER_CURVE('S'),
    QUADRATIC_BEZIER_CURVE('Q'),
    SYMMETRIC_QUADRATIC_BEZIER_CURVE('T');

    companion object {
        fun findBySvgCommand(svgCommand: Char) = entries.find { it.svgCommand == svgCommand }
    }
}