package expression;

import java.util.Set;

public class Variable implements UltraExpression {
    private final String name;
    private static final Set<String> allowedNames = Set.of("x", "y", "z");

    public Variable(String name) {
        if (!allowedNames.contains(name)) {
            throw new IllegalArgumentException("The variable name " + name + " is not allowed");
        }
        this.name = name;
    }

    @Override
    public int evaluate(int value) {
        return value;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        if ("x".equals(name)) {
            return x;
        } else if ("y".equals(name)) {
            return y;
        }
        return z;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object that) {
        return that != null && that.getClass() == this.getClass() && ((Variable) that).name.equals(this.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
