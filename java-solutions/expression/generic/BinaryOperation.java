package expression.generic;

public abstract class BinaryOperation extends Operation {
    protected BinaryOperation(TripleExpression first, TripleExpression second) {
        super(first, second);
    }

    protected abstract <T> T doOperation(T first, T second, Calculator<T> calculator);

    @Override
    public <T> T evaluate(int x, int y, int z, Calculator<T> calculator) {
        return doOperation(operands[0].evaluate(x, y, z, calculator), operands[1].evaluate(x, y, z, calculator), calculator);
    }
}
