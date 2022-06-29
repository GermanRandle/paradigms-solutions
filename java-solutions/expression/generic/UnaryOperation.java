package expression.generic;

public abstract class UnaryOperation extends Operation {
    public UnaryOperation(TripleExpression arg) {
        super(arg);
    }

    protected abstract <T> T doOperation(T arg, Calculator<T> calculator);

    @Override
    public <T> T evaluate(int x, int y, int z, Calculator<T> calculator) {
        return doOperation(operands[0].evaluate(x, y, z, calculator), calculator);
    }
}
