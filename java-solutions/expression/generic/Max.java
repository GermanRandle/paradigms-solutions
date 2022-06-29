package expression.generic;

public class Max extends BinaryOperation {
    public Max(TripleExpression first, TripleExpression second) {
        super(first, second);
    }

    @Override
    protected <T> T doOperation(T first, T second, Calculator<T> calculator) {
        return calculator.max(first, second);
    }
}
