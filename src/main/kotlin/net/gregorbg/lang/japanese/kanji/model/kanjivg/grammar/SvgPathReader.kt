package net.gregorbg.lang.japanese.kanji.model.kanjivg.grammar

import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command.Path
import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command.PathBuilder
import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command.PathCommand
import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command.svgPath
import net.gregorbg.lang.japanese.kanji.util.TypeUtils
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree

class SvgPathReader : SvgPathBaseVisitor<Path>() {
    private val coordinateReader = CoordinateReader()
    private val coordinatePairReader = CoordinatePairReader()
    private val coordinatePairDoubleReader = CoordinatePairDoubleReader()
    private val coordinatePairTripletReader = CoordinatePairTripletReader()

    private val coordinatePairSequenceReader = CoordinatePairSequenceReader()

    private abstract inner class CoordinateRecursiveReader<T>(
        private val innerReader: SvgPathVisitor<T>,
    ) : SvgPathBaseVisitor<List<T>>() {
        protected fun <C : ParseTree> visitRecursive(
            ctx: C,
            ctxSingleFn: C.() -> ParseTree,
            ctxRecurseFn: C.() -> Iterable<ParseTree>,
        ): List<T> {
            val coordinatePair = this.innerReader.visit(ctx.ctxSingleFn())
            val coordinatePairs = ctx.ctxRecurseFn().flatMap { this.visit(it) }

            return listOf(coordinatePair) + coordinatePairs
        }
    }

    private inner class CoordinateReader : SvgPathBaseVisitor<Float>() {
        override fun visitCoordinate(ctx: SvgPathParser.CoordinateContext): Float {
            return ctx.text.toFloat()
        }
    }

    private inner class CoordinateSequenceReader : CoordinateRecursiveReader<Float>(this.coordinateReader) {
        override fun visitCoordinateSequence(ctx: SvgPathParser.CoordinateSequenceContext): List<Float> {
            return this.visitRecursive(
                ctx,
                SvgPathParser.CoordinateSequenceContext::coordinate,
                SvgPathParser.CoordinateSequenceContext::coordinateSequence
            )
        }
    }

    private inner class CoordinatePairReader : SvgPathBaseVisitor<Pair<Float, Float>>() {
        private val coordinateReader = CoordinateReader()

        override fun visitCoordinatePair(ctx: SvgPathParser.CoordinatePairContext): Pair<Float, Float> {
            return Pair(
                this.coordinateReader.visit(ctx.coordinate(0)),
                this.coordinateReader.visit(ctx.coordinate(1)),
            )
        }
    }

    private inner class CoordinatePairSequenceReader : CoordinateRecursiveReader<Pair<Float, Float>>(this.coordinatePairReader) {
        override fun visitCoordinatePairSequence(ctx: SvgPathParser.CoordinatePairSequenceContext): List<Pair<Float, Float>> {
            return this.visitRecursive(
                ctx,
                SvgPathParser.CoordinatePairSequenceContext::coordinatePair,
                SvgPathParser.CoordinatePairSequenceContext::coordinatePairSequence,
            )
        }
    }

    private inner class CoordinatePairDoubleReader : SvgPathBaseVisitor<Pair<Pair<Float, Float>, Pair<Float, Float>>>() {
        private val coordinatePairReader = CoordinatePairReader()

        override fun visitCoordinatePairDouble(ctx: SvgPathParser.CoordinatePairDoubleContext): Pair<Pair<Float, Float>, Pair<Float, Float>> {
            return Pair(
                this.coordinatePairReader.visit(ctx.coordinatePair(0)),
                this.coordinatePairReader.visit(ctx.coordinatePair(1)),
            )
        }
    }

    private inner class CoordinatePairDoubleSequenceReader : CoordinateRecursiveReader<Pair<Pair<Float, Float>, Pair<Float, Float>>>(this.coordinatePairDoubleReader) {
        override fun visitCoordinatePairDoubleSequence(ctx: SvgPathParser.CoordinatePairDoubleSequenceContext): List<Pair<Pair<Float, Float>, Pair<Float, Float>>> {
            return this.visitRecursive(
                ctx,
                SvgPathParser.CoordinatePairDoubleSequenceContext::coordinatePairDouble,
                SvgPathParser.CoordinatePairDoubleSequenceContext::coordinatePairDoubleSequence,
            )
        }
    }

    private inner class CoordinatePairTripletReader : SvgPathBaseVisitor<Triple<Pair<Float, Float>, Pair<Float, Float>, Pair<Float, Float>>>() {
        private val coordinatePairReader = CoordinatePairReader()

        override fun visitCoordinatePairTriplet(ctx: SvgPathParser.CoordinatePairTripletContext): Triple<Pair<Float, Float>, Pair<Float, Float>, Pair<Float, Float>> {
            return Triple(
                this.coordinatePairReader.visit(ctx.coordinatePair(0)),
                this.coordinatePairReader.visit(ctx.coordinatePair(1)),
                this.coordinatePairReader.visit(ctx.coordinatePair(2)),
            )
        }
    }

    private inner class CoordinatePairTripletSequenceReader : CoordinateRecursiveReader<Triple<Pair<Float, Float>, Pair<Float, Float>, Pair<Float, Float>>>(this.coordinatePairTripletReader) {
        override fun visitCoordinatePairTripletSequence(ctx: SvgPathParser.CoordinatePairTripletSequenceContext): List<Triple<Pair<Float, Float>, Pair<Float, Float>, Pair<Float, Float>>> {
            return this.visitRecursive(
                ctx,
                SvgPathParser.CoordinatePairTripletSequenceContext::coordinatePairTriplet,
                SvgPathParser.CoordinatePairTripletSequenceContext::coordinatePairTripletSequence,
            )
        }
    }

    private inner class DrawingCommandParser(
        private val pathBuilder: PathBuilder
    ) : SvgPathBaseVisitor<PathCommand<*>>() {
        private val coordinateSequenceReader = CoordinateSequenceReader()
        private val coordinatePairSequenceReader = CoordinatePairSequenceReader()
        private val coordinatePairDoubleSequenceReader = CoordinatePairDoubleSequenceReader()
        private val coordinatePairTripletSequenceReader = CoordinatePairTripletSequenceReader()

        protected fun <C : ParseTree, T> visitExecutingCommands(
            ctx: C,
            coordinateReader: SvgPathVisitor<List<T>>,
            coordinateContextFn: C.() -> ParseTree,
            coordinatePackingFn: (T) -> Collection<Float>
        ): PathCommand<*> {
            val commandChar = ctx.text.first()
            val coordinateSequence = coordinateReader.visit(ctx.coordinateContextFn()).orEmpty()

            if (coordinateSequence.isEmpty()) {
                return this.pathBuilder(commandChar)
            }

            val executedCommands = coordinateSequence.map {
                val packedCoordinates = coordinatePackingFn(it).toFloatArray()
                this.pathBuilder(commandChar, *packedCoordinates)
            }

            return executedCommands.last()
        }

        override fun visitMoveTo(ctx: SvgPathParser.MoveToContext): PathCommand<*> {
            return this.visitExecutingCommands(
                ctx,
                this.coordinatePairSequenceReader,
                SvgPathParser.MoveToContext::coordinatePairSequence,
                Pair<Float, Float>::toList
            )
        }

        override fun visitClosePath(ctx: SvgPathParser.ClosePathContext): PathCommand<*> {
            return this.visitExecutingCommands(
                ctx,
                this.coordinateSequenceReader,
                TypeUtils::identity
            ) { listOf(it) }
        }

        override fun visitLineTo(ctx: SvgPathParser.LineToContext): PathCommand<*> {
            return this.visitExecutingCommands(
                ctx,
                this.coordinatePairSequenceReader,
                SvgPathParser.LineToContext::coordinatePairSequence,
                Pair<Float, Float>::toList
            )
        }

        override fun visitHorizontalLineTo(ctx: SvgPathParser.HorizontalLineToContext): PathCommand<*> {
            return this.visitExecutingCommands(
                ctx,
                this.coordinateSequenceReader,
                SvgPathParser.HorizontalLineToContext::coordinateSequence
            ) { listOf(it) }
        }

        override fun visitVerticalLineTo(ctx: SvgPathParser.VerticalLineToContext): PathCommand<*> {
            return this.visitExecutingCommands(
                ctx,
                this.coordinateSequenceReader,
                SvgPathParser.VerticalLineToContext::coordinateSequence
            ) { listOf(it) }
        }

        override fun visitCurveTo(ctx: SvgPathParser.CurveToContext): PathCommand<*> {
            return this.visitExecutingCommands(
                ctx,
                this.coordinatePairTripletSequenceReader,
                SvgPathParser.CurveToContext::coordinatePairTripletSequence
            ) { it.toList().flatMap(Pair<Float, Float>::toList) }
        }

        override fun visitSmoothCurveTo(ctx: SvgPathParser.SmoothCurveToContext): PathCommand<*> {
            return this.visitExecutingCommands(
                ctx,
                this.coordinatePairDoubleSequenceReader,
                SvgPathParser.SmoothCurveToContext::coordinatePairDoubleSequence
            ) { it.toList().flatMap(Pair<Float, Float>::toList) }
        }

        override fun visitQuadraticBezierCurveTo(ctx: SvgPathParser.QuadraticBezierCurveToContext): PathCommand<*> {
            return this.visitExecutingCommands(
                ctx,
                this.coordinatePairDoubleSequenceReader,
                SvgPathParser.QuadraticBezierCurveToContext::coordinatePairDoubleSequence
            ) { it.toList().flatMap(Pair<Float, Float>::toList) }
        }

        override fun visitSmoothQuadraticBezierCurveTo(ctx: SvgPathParser.SmoothQuadraticBezierCurveToContext): PathCommand<*> {
            return this.visitExecutingCommands(
                ctx,
                this.coordinatePairSequenceReader,
                SvgPathParser.SmoothQuadraticBezierCurveToContext::coordinatePairSequence,
                Pair<Float, Float>::toList
            )
        }
    }

    override fun visitSvgPath(ctx: SvgPathParser.SvgPathContext): Path {
        val coordinatePairSequence = this.coordinatePairSequenceReader.visit(ctx.moveTo().coordinatePairSequence())
        val startCoordinates = coordinatePairSequence.last()

        return svgPath(startCoordinates.first, startCoordinates.second) {
            val drawingCommandParser = DrawingCommandParser(this)

            for (drawingCtx in ctx.drawingCommand()) {
                drawingCommandParser.visit(drawingCtx)
            }
        }
    }

    companion object {
        val READER = SvgPathReader()

        fun parse(input: String): Path {
            val charStream = CharStreams.fromString(input)
            val lexer = SvgPathLexer(charStream)

            val tokens = CommonTokenStream(lexer)
            val parser = SvgPathParser(tokens)

            val tree = parser.svgPath()

            return READER.visit(tree)
        }
    }
}
