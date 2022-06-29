package expression.generic;

public interface TripleExpression {
    <T> T evaluate(int x, int y, int z, Calculator<T> calculator);
}
