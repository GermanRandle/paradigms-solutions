package expression.exceptions;

import expression.Divide;
import expression.UltraExpression;

public class CheckedDivide extends Divide {
    public CheckedDivide(UltraExpression first, UltraExpression second) {
        super(first, second);
    }

    public static boolean check(int x, int y) {
        return checkDivisionByZero(x, y) && checkOverflow(x, y);
    }

    public static boolean checkDivisionByZero(int x, int y) {
        return y != 0;
    }

    public static boolean checkOverflow(int x, int y) {
        return x != Integer.MIN_VALUE || y != -1;
    }

    @Override
    public int doOperation(int x, int y) {
        if (!checkDivisionByZero(x, y)) {
            throw new DivisionByZeroException("Division by zero found");
        }
        if (!checkOverflow(x, y)) {
            throw new OverflowException("Division overflow", x, y);
        }
        return super.doOperation(x, y);
    }
}
