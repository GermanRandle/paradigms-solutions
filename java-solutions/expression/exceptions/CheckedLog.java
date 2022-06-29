package expression.exceptions;

import expression.BinaryOperation;
import expression.Const;
import expression.UltraExpression;

public class CheckedLog extends BinaryOperation {
    public CheckedLog(UltraExpression first, UltraExpression second) {
        super("//", 3000, false, first, second);
    }

    @Override
    protected int doOperation(int x, int y) {
        if (y <= 1 || x <= 0) {
            throw new InvalidLogarithmException("Invalid logarithm", x, y);
        }
        int result = 0;
        for (int exp = 1; exp < 32; exp++) {
            CheckedPow pow = new CheckedPow(new Const(y), new Const(exp));
            try {
                if (pow.evaluate(0) <= x) {
                    result++;
                }
            } catch (OverflowException e) {
                break;
            }
        }
        return result;
    }

    @Override
    protected boolean needBracketsRight() {
        if (!(operands[1] instanceof BinaryOperation)) {
            return false;
        }
        return ((BinaryOperation) operands[1]).getPriority() <= this.priority;
    }
}
