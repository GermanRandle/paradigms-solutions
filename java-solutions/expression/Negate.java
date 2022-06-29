package expression;

public class Negate extends UnaryOperation {
    public Negate(UltraExpression operand) {
        super("-", 500, operand);
    }

    @Override
    protected int doOperation(int x) {
        return -x;
    }
}
