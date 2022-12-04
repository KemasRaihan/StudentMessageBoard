package network;

import java.util.Scanner;

public class ModifiedScanner {
    public ModifiedScanner() {
    }

    public static char nextChar() {
        Scanner scanner = new Scanner(System.in);
        char c = scanner.next().charAt(0);
        return c;
    }
}
