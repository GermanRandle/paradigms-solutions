package expression;

public class Main {
    public static void main(String[] args) {
        UltraExpression line = new Subtract(new Multiply(new Const(2), new Variable("x")), new Const(1));
        UltraExpression parabola = new Subtract(new Multiply(new Variable("x"), new Variable("x")), line);
        System.out.println(parabola.evaluate(Integer.parseInt(args[0])));
    }
}
