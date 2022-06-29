package expression;

public class ZeroBitsLeading extends UnaryOperation {
    public ZeroBitsLeading(UltraExpression operand) {
        super("l0", 500, operand);
    }

    @Override
    protected int doOperation(int x) {
        int result = 0;
        for (int i = 0; i < 32; i++) {
            if ((x & (1 << (31 - i))) == 0) {
                result++;
            } else {
                break;
            }
        }
        return result;
    }
}
