package expression.exceptions;

import expression.BinaryOperation;
import expression.UltraExpression;

public class CheckedPow extends BinaryOperation {
    public CheckedPow(UltraExpression first, UltraExpression second) {
        super("**", 3000, false, first, second);
    }

    private int binPow(int x, int y, int startX, int startY) {
        // Just an algorithm, thinks that values are correct
        if (y == 0) {
            return 1;
        }
        if (y == 1) {
            return x;
        }
        int firstHalf = binPow(x, y / 2, startX, startY);
        int secondHalf = firstHalf;
        if (y % 2 == 1) {
            if (!CheckedMultiply.check(secondHalf, x)) {
                throw new OverflowException("Power overflow occurred (the result is too big)", startX, startY);
            }
            secondHalf *= x;
        }
        if (!CheckedMultiply.check(firstHalf, secondHalf)) {
            throw new OverflowException("Power overflow occurred (the result is too big)", startX, startY);
        }
        return firstHalf * secondHalf;
    }

    @Override
    protected int doOperation(int x, int y) {
        if (x == 0 && y == 0) {
            throw new InvalidPowException("Undefined value", x, y);
        }
        if (y < 0) {
            throw new InvalidPowException("Undefined value in integers", x, y);
        }
        return binPow(x, y, x, y);
    }

    @Override
    protected boolean needBracketsRight() {
        if (!(operands[1] instanceof BinaryOperation)) {
            return false;
        }
        return ((BinaryOperation) operands[1]).getPriority() <= this.priority;
    }
}
