package expression.generic;

public class Subtract extends BinaryOperation {
    public Subtract(TripleExpression first, TripleExpression second) {
        super(first, second);
    }

    @Override
    protected <T> T doOperation(T first, T second, Calculator<T> calculator) {
        return calculator.subtract(first, second);
    }
}
