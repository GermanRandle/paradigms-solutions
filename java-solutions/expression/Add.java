package expression;

public class Add extends BinaryOperation {
    public Add(UltraExpression first, UltraExpression second) {
        super("+", 1000, true, first, second);
    }

    @Override
    public int doOperation(int x, int y) {
        return x + y;
    }

    @Override
    protected boolean needBracketsRight() {
        if (!(operands[1] instanceof BinaryOperation)) {
            return false;
        }
        return ((BinaryOperation) operands[1]).priority < this.priority;
    }
}
