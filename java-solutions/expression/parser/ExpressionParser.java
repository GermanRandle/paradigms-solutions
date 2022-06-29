package expression.parser;

import expression.*;

public final class ExpressionParser implements TripleParser {
    @Override
    public UltraExpression parse(String expression) {
        StringSource source = new StringSource(expression);
        ParserBody parser = new ParserBody(source);
        return parser.parse();
    }

    private static class ParserBody extends BaseParser {
        public ParserBody(StringSource source) {
            super(source);
        }

        private UltraExpression parse() {
            return parse0();
        }

        private UltraExpression parse0() {
            UltraExpression arg = parse1();
            while (true) {
                if (take('<')) {
                    expect('<');
                    arg = new ShiftLeft(arg, parse1());
                } else if (take('>')) {
                    expect('>');
                    if (take('>')) {
                        arg = new ShiftRightArithmetic(arg, parse1());
                    } else {
                        arg = new ShiftRight(arg, parse1());
                    }
                } else {
                    break;
                }
            }
            return arg;
        }

        private UltraExpression parse1() {
            UltraExpression arg = parse2();
            while (true) {
                if (take('+')) {
                    arg = new Add(arg, parse2());
                } else if (take('-')) {
                    arg = new Subtract(arg, parse2());
                } else {
                    break;
                }
            }
            return arg;
        }

        private UltraExpression parse2() {
            UltraExpression arg = parse3();
            while (true) {
                if (take('*')) {
                    arg = new Multiply(arg, parse3());
                } else if (take('/')) {
                    arg = new Divide(arg, parse3());
                } else {
                    break;
                }
            }
            return arg;
        }

        private UltraExpression parse3() {
            skipWhitespaces();
            if (take('-')) {
                if (between('0', '9')) {
                    return parseConst(true);
                }
                skipWhitespaces();
                return new Negate(parse3());
            } else if (take('l')) {
                expect('0');
                skipWhitespaces();
                return new ZeroBitsLeading(parse3());
            } else if (take('t')) {
                expect('0');
                skipWhitespaces();
                return new ZeroBitsTailing(parse3());
            }
            return parse4();
        }

        private UltraExpression parse4() {
            skipWhitespaces();
            if (take('(')) {
                UltraExpression result = parse0();
                skipWhitespaces();
                expect(')');
                skipWhitespaces();
                return result;
            }
            if (between('0', '9') || test('-')) {
                return parseConst(false);
            }
            if (between('x', 'z')) {
                return parseVariable();
            }
            error("Unexpected value");
            return null;
        }

        private UltraExpression parseConst(boolean negative) {
            int result = 0;
            if (negative) {
                while (between('0', '9')) {
                    result = 10 * result - (take() - '0');
                }
            } else {
                while (between('0', '9')) {
                    result = 10 * result + (take() - '0');
                }
            }
            skipWhitespaces();
            return new Const(result);
        }

        private UltraExpression parseVariable() {
            String name = String.valueOf(take());
            skipWhitespaces();
            return new Variable(name);
        }

        private void skipWhitespaces() {
            while (takeWhitespaceSymbol()) {
                // Skip
            }
        }
    }
}
