package expression.generic;

public class GenericTabulator implements Tabulator {
    private static Calculator<?> getCalculator(String mode) throws IllegalArgumentException {
        switch (mode) {
            case "i":
                return new CheckedIntegerCalculator();
            case "d":
                return new DoubleCalculator();
            case "bi":
                return new BigIntegerCalculator();
            case "u":
                return new IntegerCalculator();
            case "l":
                return new LongCalculator();
            case "t":
                return new TruncatedCalculator();
            default:
                throw new IllegalArgumentException("Unknown calculating mode");
        }
    }

    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws IllegalArgumentException {
        ExpressionParser parser = new ExpressionParser();
        TripleExpression parsedExpression = parser.parse(expression);
        Object[][][] result = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        Calculator<?> calculator = getCalculator(mode);
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                for (int z = z1; z <= z2; z++) {
                    try {
                        result[x - x1][y - y1][z - z1] = parsedExpression.evaluate(x, y, z, calculator);
                    } catch (ArithmeticException e) {
                        result[x - x1][y - y1][z - z1] = null;
                    }
                }
            }
        }
        return result;
    }
}
