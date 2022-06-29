package expression.generic;

public class TruncatedCalculator extends IntegerCalculator {
    private static int truncate(int value) {
        return value - value % 10;
    }

    @Override
    public Integer add(Integer x, Integer y) {
        return truncate(x + y);
    }

    @Override
    public Integer subtract(Integer x, Integer y) {
        return truncate(x - y);
    }

    @Override
    public Integer multiply(Integer x, Integer y) {
        return truncate(x * y);
    }

    @Override
    public Integer divide(Integer x, Integer y) {
        return truncate(x / y);
    }

    @Override
    public Integer fromInt(int x) {
        return truncate(x);
    }

    @Override
    public Integer count(Integer x) {
        return truncate(Integer.bitCount(x));
    }

    @Override
    public Integer negate(Integer x) {
        return truncate(-x);
    }
}
