package expression;

public abstract class Operation implements UltraExpression {
    protected final UltraExpression[] operands;
    protected final String sign;
    private final int hash;
    protected final int priority;
    private final static int hashBase = 239;
    private final static int somePrime = 17;

    protected Operation(String sign, int priority, UltraExpression... operands) {
        this.sign = sign;
        this.priority = priority;
        this.operands = operands;
        int hashPolynom = 1;
        int countingHash = 0;
        for (int i = 0; i < operands.length; i++) {
            countingHash += operands[i].hashCode() * hashPolynom;
            hashPolynom *= hashBase;
        }
        hash = countingHash + sign.hashCode() * somePrime;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int hashCode() {
        return hash;
    }
}
