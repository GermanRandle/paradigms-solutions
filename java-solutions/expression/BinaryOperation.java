package expression;

public abstract class BinaryOperation extends Operation {
    protected final boolean associative;

    public BinaryOperation(String sign, int priority, boolean associative, UltraExpression arg1, UltraExpression arg2) {
        super(sign, priority, arg1, arg2);
        this.associative = associative;
    }

    protected abstract int doOperation(int x, int y);
    protected abstract boolean needBracketsRight();

    @Override
    public int evaluate(int value) {
        return doOperation(operands[0].evaluate(value), operands[1].evaluate(value));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return doOperation(operands[0].evaluate(x, y, z), operands[1].evaluate(x, y, z));
    }

    @Override
    public String toString() {
        return "(" + operands[0] + " " + sign + " " + operands[1] + ")";
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Operation
                && operands[0].equals(((BinaryOperation) other).operands[0])
                && operands[1].equals(((BinaryOperation) other).operands[1])
                && sign.equals(((BinaryOperation) other).sign);
    }

    @Override
    public String toMiniString() {
        StringBuilder sb = new StringBuilder();
        if (needBracketsLeft()) {
            sb.append('(').append(operands[0].toMiniString()).append(')');
        } else {
            sb.append(operands[0].toMiniString());
        }
        sb.append(' ').append(sign).append(' ');
        if (needBracketsRight()) {
            sb.append('(').append(operands[1].toMiniString()).append(')');
        } else {
            sb.append(operands[1].toMiniString());
        }
        return sb.toString();
    }

    private boolean needBracketsLeft() {
        if (!(operands[0] instanceof BinaryOperation)) {
            return false;
        }
        return ((BinaryOperation) operands[0]).priority < this.priority;
    }
}
