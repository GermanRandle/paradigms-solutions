package expression.generic;

public class Add extends BinaryOperation {
    public Add(TripleExpression first, TripleExpression second) {
        super(first, second);
    }

    @Override
    protected <T> T doOperation(T first, T second, Calculator<T> calculator) {
        return calculator.add(first, second);
    }
}