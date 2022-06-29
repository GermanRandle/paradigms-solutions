package expression;

public class Subtract extends BinaryOperation {
    public Subtract(UltraExpression first, UltraExpression second) {
        super("-", 1000, false, first, second);
    }

    @Override
    public int doOperation(int x, int y) {
        return x - y;
    }

    @Override
    protected boolean needBracketsRight() {
        if (!(operands[1] instanceof BinaryOperation)) {
            return false;
        }
        return ((BinaryOperation) operands[1]).priority <= this.priority;
    }
}
