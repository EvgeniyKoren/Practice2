package ua.koren.primeNumbers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PrimeFinderExecutor {

    private List<Integer> primeNumbers;

    public class Finder implements Callable<List<Integer>> {
        private int start;
        private int end;
        private int step;

        public Finder(int start, int end, int step) {
            this.start = start;
            this.end = end;
            this.step = step;
        }

        @Override
        public List<Integer> call() {

            return findPrimesAndSaveResult(start, end, step);
        }
    }

    public class Seeker implements Callable<Void> {
        private int start;
        private int end;
        private int step;

        public Seeker(int start, int end, int step) {
            this.start = start;
            this.end = end;
            this.step = step;
        }

        @Override
        public Void call() {
            findPrimes(start, end, step);
            return null;
        }
    }

    /*
     * This method save numbers into its own collection
     */
    public List<Integer> findByExecutor(int start, int end, int threadsNumber) throws ExecutionException, InterruptedException {
        List<Integer> result = new ArrayList<>();
        int delimiter = start;
        ExecutorService executorService = Executors.newFixedThreadPool(threadsNumber);

        Future<List<Integer>>[] futures = new Future[threadsNumber];
        for (int i = 0; i < futures.length; i++) {
            futures[i] = executorService.submit(new Finder(delimiter, end, threadsNumber));
            delimiter++;
        }

        for (Future<List<Integer>> future : futures) {
            result.addAll(future.get());
        }

        executorService.shutdown();
        return result;
    }

    /*
     * This method save numbers into common collection
     */
    public List<Integer> findByExecutorAndCommonRepo(int start, int end, int threadsNumber) throws ExecutionException, InterruptedException {
        primeNumbers = new ArrayList<>();
        int delimiter = start;
        ExecutorService executorService = Executors.newFixedThreadPool(threadsNumber);

        Future<Void>[] futures = new Future[threadsNumber];
        for (int i = 0; i < threadsNumber; i++) {
            futures[i] = executorService.submit(new Seeker(delimiter, end, threadsNumber));
            delimiter++;
        }

        for (Future<Void> future : futures) {
            future.get();
        }

        executorService.shutdown();
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
        int[] input = new int[]{1000, 3000, 5};
        PrimeFinderExecutor primeFinder = new PrimeFinderExecutor();
        // test findExecutor method
        List<Integer> list1 = null;
        long start1 = System.currentTimeMillis();
        try {
            list1 = primeFinder.findByExecutor(input[0], input[1], input[2]);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        long finish1 = System.currentTimeMillis();
        Util.printResult(list1);

        // test findByExecutorAndCommonRepo method
        List<Integer> list2 = null;
        long start2 = System.currentTimeMillis();
        try {
            list2 = primeFinder.findByExecutorAndCommonRepo(input[0], input[1], input[2]);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        long finish2 = System.currentTimeMillis();
        Util.printResult(list2);
        System.out.println("Working time findByExecutor: " + (finish1 - start1) + "ms");
        System.out.println("Working time findByExecutorAndCommonRepo: " + (finish2 - start2) + "ms");
    }
}
