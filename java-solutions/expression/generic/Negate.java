package expression.generic;

public class Negate extends UnaryOperation {
    public Negate(TripleExpression arg) {
        super(arg);
    }

    @Override
    protected <T> T doOperation(T arg, Calculator<T> calculator) {
        return calculator.negate(arg);
    }
}
