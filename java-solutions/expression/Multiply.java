package expression;

public class Multiply extends BinaryOperation {
    public Multiply(UltraExpression first, UltraExpression second) {
        super("*", 2000, true, first, second);
    }

    @Override
    public int doOperation(int x, int y) {
        return x * y;
    }

    @Override
    protected boolean needBracketsRight() {
        if (!(operands[1] instanceof BinaryOperation)) {
            return false;
        }
        BinaryOperation rightOp = (BinaryOperation) operands[1];
        return rightOp.priority < this.priority ||
                (rightOp.priority == this.priority && !rightOp.associative);
    }
}
