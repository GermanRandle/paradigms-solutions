package expression.exceptions;

import expression.UltraExpression;
import expression.UnaryOperation;

public class CheckedAbs extends UnaryOperation {
    public CheckedAbs(UltraExpression arg) {
        super("abs", 500, arg);
    }

    @Override
    protected int doOperation(int x) {
        if (x == Integer.MIN_VALUE) {
            throw new OverflowException("Overflow of absolute value", x);
        }
        if (x >= 0) {
            return x;
        }
        return -x;
    }
}
