package expression.generic;

public class Divide extends BinaryOperation {
    public Divide(TripleExpression first, TripleExpression second) {
        super(first, second);
    }

    @Override
    protected <T> T doOperation(T first, T second, Calculator<T> calculator) {
        return calculator.divide(first, second);
    }
}
