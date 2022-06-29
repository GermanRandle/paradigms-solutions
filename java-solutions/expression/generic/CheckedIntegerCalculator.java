package expression.generic;

import expression.exceptions.*;

public class CheckedIntegerCalculator extends IntegerCalculator {
    @Override
    public Integer add(Integer x, Integer y) {
       if (!CheckedAdd.check(x, y)) {
           throw new ArithmeticException("Add overflow");
       }
       return x + y;
    }

    @Override
    public Integer subtract(Integer x, Integer y) {
        if (!CheckedSubtract.check(x, y)) {
            throw new ArithmeticException("Subtract overflow");
        }
        return x - y;
    }

    @Override
    public Integer multiply(Integer x, Integer y) {
        if (!CheckedMultiply.check(x, y)) {
            throw new ArithmeticException("Multiply overflow");
        }
        return x * y;
    }

    @Override
    public Integer divide(Integer x, Integer y) {
        if (!CheckedDivide.check(x, y)) {
            throw new ArithmeticException("Divide overflow");
        }
        return x / y;
    }

    @Override
    public Integer negate(Integer x) {
        if (!CheckedNegate.check(x)) {
            throw new ArithmeticException("Negate overflow");
        }
        return -x;
    }
}
