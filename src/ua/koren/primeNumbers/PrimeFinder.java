package ua.koren.primeNumbers;

import java.util.ArrayList;
import java.util.List;

public class PrimeFinder {

    private List<Integer> primeNumbers;

    /*
     * This method save numbers into its own collection
     */
    public List<Integer> findByOwnRepo(int start, int end, int threadsNumber) {
        List<Integer> result = new ArrayList<>();
        int delimiter = start;

        for (int i = 0; i < threadsNumber; i++) {
            int startPoint = delimiter;
            final List<Integer>[] personalResult = new List[]{null};
            Thread finder = new Thread(new Runnable() {
                @Override
                public void run() {
                    personalResult[0] = findPrimesAndSaveResult(startPoint, end, threadsNumber);
                }
            });
            finder.start();
            try {
                finder.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            result.addAll(personalResult[0]);
            delimiter++;
        }

        return result;
    }

    /*
     * This method save numbers into common collection
     */
    public List<Integer> findByCommonRepo(int start, int end, int threadsNumber) {
        primeNumbers = new ArrayList<>();
        int delimiter = start;

        for (int i = 0; i < threadsNumber; i++) {
            int startPoint = delimiter;
            Thread finder = new Thread(new Runnable() {
                @Override
                public void run() {
                    findPrimes(startPoint, end, threadsNumber);
                }
            });
            finder.start();
            try {
                finder.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            delimiter++;
        }

        return primeNumbers;
    }

    private void findPrimes(int startPoint, int endPoint, int step) {
        for (int j = startPoint; j < endPoint; j+=step) {
            if (Util.isPrime(j)) {
                primeNumbers.add(j);
            }
        }
    }

    private List<Integer> findPrimesAndSaveResult(int startPoint, int endPoint, int step) {
        List<Integer> primes = new ArrayList<>();
        for (int j = startPoint; j < endPoint; j+=step) {
            if (Util.isPrime(j)) {
                primes.add(j);
            }
        }
        return primes;
    }

    public static void main(String[] args) {
        int[] input = Util.getInputData();
        PrimeFinder primeFinder = new PrimeFinder();

        // test findByOwnRepo method
        long start1 = System.currentTimeMillis();
        List<Integer> list1 = primeFinder.findByOwnRepo(input[0], input[1], input[2]);
        long finish1 = System.currentTimeMillis();
        Util.printResult(list1);

        // test findByCommonRepo method
        long start2 = System.currentTimeMillis();
        List<Integer> list2 = primeFinder.findByCommonRepo(input[0], input[1], input[2]);
        long finish2 = System.currentTimeMillis();
        Util.printResult(list2);

        System.out.println("Working time findUseOwnRepo: " + (finish1 - start1) + "ms");
        System.out.println("Working time findUseCommonRepo: " + (finish2 - start2) + "ms");
    }
}
