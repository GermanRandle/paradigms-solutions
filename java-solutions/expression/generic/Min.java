package expression.generic;

public class Min extends BinaryOperation {
    public Min(TripleExpression first, TripleExpression second) {
        super(first, second);
    }

    @Override
    protected <T> T doOperation(T first, T second, Calculator<T> calculator) {
        return calculator.min(first, second);
    }
}
