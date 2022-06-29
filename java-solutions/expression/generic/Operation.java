package expression.generic;

public abstract class Operation implements TripleExpression {
    protected final TripleExpression[] operands;

    protected Operation(TripleExpression... operands) {
        this.operands = operands;
    }
}