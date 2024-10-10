package net.gregorbg.lang.japanese.kanji.model.kanjivg.path

import kotlin.math.max

data class Circle(val center: GeomPoint, val radius: Float) {
    operator fun contains(p: GeomPoint): Boolean {
        return isPointInsideCircle(p, this)
    }

    companion object {
        fun isPointInsideCircle(point: GeomPoint, circle: Circle): Boolean {
            return point.segmentTo(circle.center).norm() <= circle.radius
        }

        fun circleFromTwoPoints(p1: GeomPoint, p2: GeomPoint): Circle {
            val center = (p1 + p2) / 2
            val radius = p1.segmentTo(p2).norm() / 2

            return Circle(center, radius)
        }

        fun circleFromThreePoints(p1: GeomPoint, p2: GeomPoint, p3: GeomPoint): Circle {
            val a = p1.dotProduct(p1)
            val b = p2.dotProduct(p2)
            val c = p3.dotProduct(p3)

            val d = 2 * (p1.crossProduct(p2) + p2.crossProduct(p3) + p3.crossProduct(p1))

            val ux = (a * (p2.y - p3.y) + b * (p3.y - p1.y) + c * (p1.y - p2.y)) / d
            val uy = (a * (p3.x - p2.x) + b * (p1.x - p3.x) + c * (p2.x - p1.x)) / d

            val center = GeomPoint(ux, uy)

            val radius = max(
                center.segmentTo(p1).norm(),
                max(
                    center.segmentTo(p2).norm(),
                    center.segmentTo(p3).norm()
                )
            )

            return Circle(center, radius)
        }

        // Tail-recursive function to find the minimal enclosing circle using Welzl's algorithm
        tailrec fun findMinimalEnclosingCircle(points: List<GeomPoint>, boundary: List<GeomPoint> = emptyList()): Circle {
            // Base cases
            return when {
                points.isEmpty() || boundary.size == 3 -> {
                    when (boundary.size) {
                        0 -> Circle(GeomPoint.origin, 0f)
                        1 -> Circle(boundary[0], 0f)
                        2 -> circleFromTwoPoints(boundary[0], boundary[1])
                        3 -> circleFromThreePoints(boundary[0], boundary[1], boundary[2])
                        else -> throw IllegalStateException("Invalid boundary size")
                    }
                }
                else -> {
                    // Pick a random point and remove it from the list
                    val p = points.first()
                    val remainingPoints = points.drop(1)

                    // Recursively find the minimal circle without the current point
                    val circle = findMinimalEnclosingCircle(remainingPoints, boundary)

                    // If the point is inside the circle, return the current circle
                    if (p in circle) {
                        circle
                    } else {
                        // Otherwise, expand the circle to include the current point
                        findMinimalEnclosingCircle(remainingPoints, boundary + p)
                    }
                }
            }
        }
    }
}