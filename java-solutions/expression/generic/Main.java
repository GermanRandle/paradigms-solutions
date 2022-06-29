package expression.generic;

public class Main {
    private static void printTable(final Object[][][] table, int x1, int y1, int z1) {
        System.out.println("x\ty\tz\tresult");
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                for (int k = 0; k < table[i][j].length; k++) {
                    System.out.print((x1 + i) + "\t" + (y1 + j) + "\t" + (z1 + k) + ":\t");
                    System.out.println(table[i][j][k]);
                }
            }
        }
    }

    public static void main(String[] args) {
        final Tabulator tabulator = new GenericTabulator();
        final Object[][][] table;
        final int x1 = -2;
        final int x2 = 2;
        final int y1 = -2;
        final int y2 = 2;
        final int z1 = -2;
        final int z2 = 2;
        try {
            table = tabulator.tabulate(args[0].substring(1), args[1], x1, x2, y1, y2, z1, z2);
        } catch (Exception e) {
            System.err.println("Error occurred while tabulating, more info: " + e.getMessage());
            return;
        }
        printTable(table, x1, y1, z1);
    }
}
