package expression.exceptions;

import expression.Negate;
import expression.UltraExpression;

public class CheckedNegate extends Negate {
    public CheckedNegate(UltraExpression arg) {
        super(arg);
    }

    public static boolean check(int x) {
        return x != Integer.MIN_VALUE;
    }

    @Override
    protected int doOperation(int x) {
        if (!check(x)) {
            throw new OverflowException("Negate overflow", x);
        }
        return super.doOperation(x);
    }
}
