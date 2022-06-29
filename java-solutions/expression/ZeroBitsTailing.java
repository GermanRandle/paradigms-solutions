package expression;

public class ZeroBitsTailing extends UnaryOperation {
    public ZeroBitsTailing(UltraExpression operand) {
        super("t0", 500, operand);
    }

    @Override
    protected int doOperation(int x) {
        int result = 0;
        for (int i = 0; i < 32; i++) {
            if ((x & (1 << i)) == 0) {
                result++;
            } else {
                break;
            }
        }
        return result;
    }
}
