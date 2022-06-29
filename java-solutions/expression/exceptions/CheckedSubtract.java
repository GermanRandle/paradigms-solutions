package expression.exceptions;

import expression.Subtract;
import expression.UltraExpression;

public class CheckedSubtract extends Subtract {
    public CheckedSubtract(UltraExpression first, UltraExpression second) {
        super(first, second);
    }

    public static boolean check(int x, int y) {
        return checkBiggerOverflow(x, y) && checkLessOverflow(x, y);
    }

    private static boolean checkBiggerOverflow(int x, int y) {
        return !(y > 0 && x < Integer.MIN_VALUE + y);
    }

    private static boolean checkLessOverflow(int x, int y) {
        return !(y < 0 && x > Integer.MAX_VALUE + y);
    }

    @Override
    public int doOperation(int x, int y) {
        if (!checkBiggerOverflow(x, y)) {
            throw new OverflowException("Subtract overflow (the result is too big)", x, y);
        }
        if (!checkLessOverflow(x, y)) {
            throw new OverflowException("Subtract overflow (the result is too small)", x, y);
        }
        return super.doOperation(x, y);
    }
}
