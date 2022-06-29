package expression.exceptions;

import expression.*;
import expression.parser.BaseParser;
import expression.parser.StringSource;

public class ExpressionParser implements TripleParser {
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
            UltraExpression result = parse0();
            skipWhitespaces();
            if (!eof()) {
                throw error("Expected end of input");
            }
            return result;
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
                    arg = new CheckedAdd(arg, parse2());
                } else if (take('-')) {
                    arg = new CheckedSubtract(arg, parse2());
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
                    arg = new CheckedMultiply(arg, parse3());
                } else if (take('/')) {
                    arg = new CheckedDivide(arg, parse3());
                } else {
                    break;
                }
            }
            return arg;
        }

        private UltraExpression parse3() {
            UltraExpression arg = parse4();
            while (true) {
                if (take('*')) {
                    if (take('*')) {
                        arg = new CheckedPow(arg, parse4());
                    } else {
                        stepBack();
                        break;
                    }
                } else if (take('/')) {
                    if (take('/')) {
                        arg = new CheckedLog(arg, parse4());
                    } else {
                        stepBack();
                        break;
                    }
                } else {
                    break;
                }
            }
            return arg;
        }

        private UltraExpression parse4() {
            skipWhitespaces();
            if (take('-')) {
                if (between('0', '9')) {
                    return parseConst(true);
                }
                skipWhitespaces();
                return new CheckedNegate(parse4());
            } else if (take('a')) {
                expect('b');
                expect('s');
                if (between('x', 'z') || between('0', '9')) {
                    throw error("No spaces between abs and argument");
                }
                skipWhitespaces();
                return new CheckedAbs(parse4());
            }
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
            throw error("Unexpected symbol");
        }

        private UltraExpression parseConst(boolean negative) {
            int result = 0;
            if (negative) {
                while (between('0', '9')) {
                    if (!CheckedMultiply.check(10, result)) {
                        throw error("Const is too small");
                    }
                    result *= 10;
                    int curDigit = take() - '0';
                    if (!CheckedSubtract.check(result, curDigit)) {
                        throw error("Const is too small");
                    }
                    result -= curDigit;
                }
            } else {
                while (between('0', '9')) {
                    if (!CheckedMultiply.check(10, result)) {
                        throw error("Const is too big");
                    }
                    result *= 10;
                    int curDigit = take() - '0';
                    if (!CheckedAdd.check(result, curDigit)) {
                        throw error("Const is too big");
                    }
                    result += curDigit;
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
