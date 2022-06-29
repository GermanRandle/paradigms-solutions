package expression.generic;

public class LongCalculator implements Calculator<Long> {
    @Override
    public Long add(Long x, Long y) {
        return x + y;
    }

    @Override
    public Long subtract(Long x, Long y) {
        return x - y;
    }

    @Override
    public Long multiply(Long x, Long y) {
        return x * y;
    }

    @Override
    public Long divide(Long x, Long y) {
        return x / y;
    }

    @Override
    public Long fromInt(int x) {
        return (long) x;
    }

    @Override
    public Long count(Long x) {
        return (long) Long.bitCount(x);
    }

    @Override
    public Long max(Long x, Long y) {
        return Long.max(x, y);
    }

    @Override
    public Long min(Long x, Long y) {
        return Long.min(x, y);
    }

    @Override
    public Long negate(Long x) {
        return -x;
    }
}
