package expression.exceptions;

import expression.*;

public class Main {
    public static void main(String[] args) {
        Variable x = new Variable("x");
        UltraExpression exp = new CheckedMultiply(new Const(1000000), x);
        for (int i = 0; i < 4; i++) {
            exp = new CheckedMultiply(exp, x);
        }
        exp = new CheckedDivide(exp, new CheckedSubtract(x, new Const(1)));
        System.out.printf("%s   %s", "x", "f");
        System.out.println();
        for (int arg = 0; arg <= 10; arg++) {
            try {
                int result = exp.evaluate(arg);
                System.out.printf("%s   %s", arg, result);
            } catch (ArithmeticException e) {
                System.out.printf("%s   %s", arg, e.getMessage());
            }
            System.out.println();
        }
    }
}
