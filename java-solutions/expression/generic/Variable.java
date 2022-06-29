package expression.generic;

import java.util.Set;

public class Variable implements TripleExpression {
    private final String name;
    private static final Set<String> allowedNames = Set.of("x", "y", "z");

    public Variable(String name) {
        if (!allowedNames.contains(name)) {
            throw new IllegalArgumentException("The variable name " + name + " is not allowed");
        }
        this.name = name;
    }

    @Override
    public <T> T evaluate(int x, int y, int z, Calculator<T> calculator) {
        if ("x".equals(name)) {
            return calculator.fromInt(x);
        } else if ("y".equals(name)) {
            return calculator.fromInt(y);
        }
        return calculator.fromInt(z);
    }
}
