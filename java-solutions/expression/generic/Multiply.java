package expression.generic;

public class Multiply extends BinaryOperation {
    public Multiply(TripleExpression first, TripleExpression second) {
        super(first, second);
    }

    @Override
    protected <T> T doOperation(T first, T second, Calculator<T> calculator) {
        return calculator.multiply(first, second);
    }
}
