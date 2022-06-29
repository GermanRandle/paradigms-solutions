package expression.generic;

public class Const implements TripleExpression {
    private final int value;

    public Const(int value) {
        this.value = value;
    }

    @Override
    public <T> T evaluate(int x, int y, int z, Calculator<T> calculator) {
        return calculator.fromInt(value);
    }
}