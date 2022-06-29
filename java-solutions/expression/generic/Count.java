package expression.generic;

public class Count extends UnaryOperation {
    public Count(TripleExpression arg) {
        super(arg);
    }

    @Override
    protected <T> T doOperation(T arg, Calculator<T> calculator) {
        return calculator.count(arg);
    }
}
