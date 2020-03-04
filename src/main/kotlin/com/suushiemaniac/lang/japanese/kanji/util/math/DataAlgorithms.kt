package com.suushiemaniac.lang.japanese.kanji.util.math

object DataAlgorithms {
    fun <T> levenshtein(here: List<T>, there: List<T>): Int {
        val matrix = levenshteinCostMatrix(there, here)
        return matrix.last().last()
    }

    fun levenshtein(here: String, there: String) = levenshtein(here.toList(), there.toList())

    fun <T> levenshteinSequences(here: List<T>, there: List<T>): List<List<EditStep>> {
        val mat = levenshteinCostMatrix(there, here)
        val tracesAndPaths = levenshteinBacktrace(mat, there.size, here.size, there, here)

        return tracesAndPaths.map { it.map(Pair<Pair<Int, Int>, EditStep>::second).reversed() }
    }

    fun levenshteinSequences(here: String, there: String) = levenshteinSequences(here.toList(), there.toList())

    private fun <T> levenshteinCostMatrix(vertical: List<T>, horizontal: List<T>): List<List<Int>> {
        return tensorBuilder(vertical.size + 1, { lnAcc ->
            val lastLine = lnAcc.last()
            val colChar = vertical[lnAcc.size - 1]

            tensorBuilder(horizontal.size + 1, { symAcc ->
                val lastScore = symAcc.last()
                val headChar = horizontal[symAcc.size - 1]

                val remove = lastScore + 1
                val insert = lastLine[symAcc.size] + 1
                val replace = lastLine[symAcc.size - 1] + (colChar != headChar).toInt()

                minOf(replace, remove, insert)
            }, listOf(lastLine.first() + 1))
        }, listOf(List(horizontal.size + 1) { it }))
    }

    private tailrec fun <T> tensorBuilder(targetDim: Int, iter: (List<T>) -> T, accu: List<T> = emptyList()): List<T> {
        if (accu.size == targetDim) {
            return accu
        }

        val nextElem = iter(accu)
        val newAccu = accu + nextElem

        return tensorBuilder(targetDim, iter, newAccu)
    }

    fun Boolean.toInt() = if (this) 1 else 0

    private fun <T> levenshteinBacktrace(matrix: List<List<Int>>, line: Int, col: Int, vertical: List<T>, horizontal: List<T>, accu: List<Pair<Pair<Int, Int>, EditStep>> = emptyList()): List<List<Pair<Pair<Int, Int>, EditStep>>> {
        if (line == 0 && col == 0) {
            return listOf(accu)
        }

        val curr = matrix[line][col]

        val nextSteps = listOfNotNull(
                nextStep(matrix, line - 1, col, vertical, horizontal, curr, EditStep.INSERT),
                nextStep(matrix, line, col - 1, vertical, horizontal, curr, EditStep.REMOVE),
                nextStep(matrix, line - 1, col - 1, vertical, horizontal, curr, EditStep.REPLACE)
        )

        return nextSteps.flatMap { (coords, op) ->
            val newAccu = accu + (coords to op)

            levenshteinBacktrace(matrix, coords.first, coords.second, vertical, horizontal, newAccu)
        }
    }

    private fun <T> nextStep(matrix: List<List<Int>>, line: Int, col: Int, vertical: List<T>, horizontal: List<T>, prev: Int, step: EditStep): Pair<Pair<Int, Int>, EditStep>? {
        if (line < 0 || col < 0) {
            return null
        }

        val curr = matrix[line][col]

        val verticalChar = vertical.getOrNull(line)
        val horizontalChar = horizontal.getOrNull(col)
        val notMatchLetter = verticalChar != horizontalChar

        val bias = when (step) {
            EditStep.INSERT -> 1
            EditStep.REMOVE -> 1
            EditStep.REPLACE -> notMatchLetter.toInt()
            EditStep.COPY -> notMatchLetter.toInt()
        }

        val nextStep = when (step) {
            EditStep.INSERT -> step
            EditStep.REMOVE -> step
            EditStep.REPLACE, EditStep.COPY -> if (notMatchLetter) EditStep.REPLACE else EditStep.COPY
        }

        return ((line to col) to nextStep).takeIf { curr == prev - bias }
    }

    enum class EditStep {
        INSERT, REMOVE, REPLACE, COPY
    }
}
