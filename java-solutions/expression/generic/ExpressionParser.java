package expression.generic;

import expression.exceptions.CheckedAdd;
import expression.exceptions.CheckedMultiply;
import expression.exceptions.CheckedNegate;
import expression.exceptions.CheckedSubtract;
import expression.parser.BaseParser;
import expression.parser.StringSource;

public class ExpressionParser {
    public TripleExpression parse(String expression) {
        StringSource source = new StringSource(expression);
        ParserBody parser = new ParserBody(source);
        return parser.parse();
    }

    private static class ParserBody extends BaseParser {
        public ParserBody(StringSource source) {
            super(source);
        }

        private TripleExpression parse() {
            TripleExpression result = parse0();
            skipWhitespaces();
            if (!eof()) {
                throw error("Expected end of input");
            }
            return result;
        }

        private TripleExpression parse0() {
            TripleExpression arg = parse1();
            while (true) {
                if (take('m')) {
                    if (take('i')) {
                        expect('n');
                        arg = new Min(arg, parse1());
                    } else if (take('a')) {
                        expect('x');
                        arg = new Max(arg, parse1());
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
            return arg;
        }

        private TripleExpression parse1() {
            TripleExpression arg = parse2();
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

        private TripleExpression parse2() {
            TripleExpression arg = parse3();
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

        private TripleExpression parse3() {
            skipWhitespaces();
            if (take('-')) {
                if (between('0', '9')) {
                    return parseConst(true);
                }
                skipWhitespaces();
                return new Negate(parse3());
            }
            if (take('c')) {
                expect('o');
                expect('u');
                expect('n');
                expect('t');
                skipWhitespaces();
                TripleExpression result = new Count(parse3());
                skipWhitespaces();
                return result;
            }
            skipWhitespaces();
            if (take('(')) {
                TripleExpression result = parse0();
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

        private TripleExpression parseConst(boolean negative) {
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

        private TripleExpression parseVariable() {
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
