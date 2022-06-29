package expression;

public abstract class UnaryOperation extends Operation {
    protected UnaryOperation(String sign, int priority, UltraExpression arg) {
        super(sign, priority, arg);
    }

    protected abstract int doOperation(int x);

    @Override
    public int evaluate(int x) {
        return doOperation(operands[0].evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return doOperation(operands[0].evaluate(x, y, z));
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof UnaryOperation
                && operands[0].equals(((UnaryOperation) other).operands[0])
                && sign.equals(((UnaryOperation) other).sign);
    }

    @Override
    public String toString() {
        return sign + "(" + operands[0].toString() + ")";
    }

    @Override
    public String toMiniString() {
        if (operands[0] instanceof BinaryOperation) {
            return sign + "(" + operands[0].toMiniString() + ")";
        }
        return sign + " " + operands[0].toMiniString();
    }
}
