package expression.exceptions;

import expression.Multiply;
import expression.UltraExpression;

public class CheckedMultiply extends Multiply {
    public CheckedMultiply(UltraExpression first, UltraExpression second) {
        super(first, second);
    }

    public static boolean check(int x, int y) {
        return checkBiggerOverflow(x, y) && checkLessOverflow(x, y);
    }

    private static boolean checkBiggerOverflow(int x, int y) {
        if (x == 0 || y == 0) {
            return true;
        }
        if (x < 0 && y < 0) {
            if (x == Integer.MIN_VALUE || y == Integer.MIN_VALUE) {
                return false;
            }
            x = -x;
            y = -y;
        } else if (x < 0) {
            return true;
        } else if (y < 0) {
            return true;
        }
        return !(x > Integer.MAX_VALUE / y);
    }

    private static boolean checkLessOverflow(int x, int y) {
        if (x > 0 && y < 0) {
            y = -y;
        } else if (x < 0 && y > 0) {
            if (x == Integer.MIN_VALUE) {
                return y == 1;
            }
            x = -x;
        } else {
            return true;
        }
        if (y == 1) {
            return true;
        }
        if (Integer.MAX_VALUE % y == y - 1) {
            return x <= Integer.MAX_VALUE / y + 1;
        } else {
            return x <= Integer.MAX_VALUE / y;
        }
    }

    @Override
    public int doOperation(int x, int y) {
        if (!checkBiggerOverflow(x, y)) {
            throw new OverflowException("Multiply overflow occurred (the result is too big)", x, y);
        }
        if (!checkLessOverflow(x, y)) {
            throw new OverflowException("Multiply overflow occurred (the result is too small)", x, y);
        }
        return super.doOperation(x, y);
    }
}
