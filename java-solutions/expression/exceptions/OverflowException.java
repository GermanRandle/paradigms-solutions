package expression.exceptions;

public class OverflowException extends ArithmeticException {
    public static String argsToString(int[] args) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length - 1; i++) {
            sb.append(args[i]).append(' ');
        }
        if (args.length != 0) {
            sb.append(args[args.length - 1]);
        }
        return sb.toString();
    }

    public OverflowException(String message, int argument, int... otherArgs) {
        super(message + ". Arguments: " + argument + " " + argsToString(otherArgs));
    }
}
