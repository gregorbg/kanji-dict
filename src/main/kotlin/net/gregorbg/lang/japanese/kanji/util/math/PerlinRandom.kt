package net.gregorbg.lang.japanese.kanji.util.math

import kotlin.random.Random

data class PerlinRandom(
    val seed: Int,
    val lowerBound: Float = -1f,
    val upperBound: Float = 1f,
) {
    private val internalSeed = Random(this.seed).nextInt()

    fun generateNoise(points: Int): List<Float> {
        return List(points) {
            this.lowerBound + (this.upperBound - this.lowerBound) * Random(this.internalSeed + it).nextFloat()
        }
    }
}
