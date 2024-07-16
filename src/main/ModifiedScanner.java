package main;

import java.util.Scanner;

public class ModifiedScanner {
    public ModifiedScanner() {
    }

    public static int choose() {
        Scanner scanner = new Scanner(System.in);
        char c = scanner.next().charAt(0);
        int option = Character.getNumericValue(c);
        return option;
    }
}
