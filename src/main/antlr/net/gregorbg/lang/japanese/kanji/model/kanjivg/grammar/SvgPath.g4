grammar SvgPath;

svgPath: moveTo drawingCommand*;

drawingCommand:
    moveTo
    | closePath
    | lineTo
    | horizontalLineTo
    | verticalLineTo
    | curveTo
    | smoothCurveTo
    | quadraticBezierCurveTo
    | smoothQuadraticBezierCurveTo
    | ellipticalArc;

moveTo:
    ('M'|'m') coordinatePairSequence;

closePath:
    ('Z'|'z');

lineTo:
    ('L'|'l') coordinatePairSequence;

horizontalLineTo:
    ('H'|'h') coordinateSequence;

verticalLineTo:
    ('V'|'v') coordinateSequence;

curveTo:
    ('C'|'c') coordinatePairTripletSequence;

smoothCurveTo:
    ('S'|'s') coordinatePairDoubleSequence;

quadraticBezierCurveTo:
    ('Q'|'q') coordinatePairDoubleSequence;

smoothQuadraticBezierCurveTo:
    ('T'|'t') coordinatePairSequence;

ellipticalArc:
    ('A'|'a') ellipticalArcArgumentSequence;

ellipticalArcArgumentSequence:
    ellipticalArcArgument (COMMA? ellipticalArcArgumentSequence)*;

ellipticalArcArgument:
    NUMBER COMMA? NUMBER COMMA? NUMBER COMMA
    FLAG COMMA? FLAG COMMA? coordinatePair;

coordinate: SIGN? NUMBER;

coordinateSequence:
    coordinate (COMMA? coordinateSequence)*;

coordinatePair: coordinate COMMA? coordinate;

coordinatePairSequence:
    coordinatePair (COMMA? coordinatePairSequence)*;

coordinatePairDouble:
    coordinatePair COMMA? coordinatePair;

coordinatePairDoubleSequence:
    coordinatePairDouble (COMMA? coordinatePairDoubleSequence)*;

coordinatePairTriplet:
    coordinatePair COMMA? coordinatePair COMMA? coordinatePair;

coordinatePairTripletSequence:
    coordinatePairTriplet (COMMA? coordinatePairTripletSequence)*;

SIGN: '+'|'-';
NUMBER: [0-9]+ ('.' [0-9]+)?;
FLAG: ('0'|'1');
COMMA: ',';
WS: [ \t\n\f\r] -> skip;
