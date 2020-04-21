package ua.koren.primeNumbers;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public final class Util {

    public static boolean isPrime(int j) {
        for (int i = 2; i < j/2 ; i++) {
            if (j % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static void printResult(List<Integer> primes) {
        Collections.sort(primes);
        System.out.println(primes.toString());
    }

    public static int[] getInputData() {
        int [] data = new int[3];
        try (Scanner scan = new Scanner(System.in)) {
            for (int i = 0; i < data.length; i++) {
                System.out.println("Enter " + (i + 1) + " parameter");
                if (scan.hasNextInt()) {
                    data[i] = scan.nextInt();
                }
                scan.nextLine();
            }
        }
        return data;
    }
}
