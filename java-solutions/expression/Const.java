package expression;

public class Const implements UltraExpression {
    private final Number value;

    public Const(int value) {
        this.value = value;
    }

    @Override
    public int evaluate(int x) {
        return (int) value;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return (int) value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object that) {
        return that != null && that.getClass() == this.getClass() && (((Const) that).value).equals(this.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
