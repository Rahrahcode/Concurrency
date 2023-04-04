package ConcurrencyProject;


import java.util.Random;

public class Concurrency {

    public static void main(String[] args) {
        int[] arr = new int[200000000];
        Random rand = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = rand.nextInt(10) + 1;
        }

        int numThreads = 4;
        int cSize = arr.length / numThreads;

        Thread[] threads = new Thread[numThreads];
        SumThread[] sumThreads = new SumThread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            sumThreads[i] = new SumThread(i * cSize, (i + 1) * cSize, arr);
            threads[i] = new Thread(sumThreads[i]);
            threads[i].start();
        }

        int parallelSum = 0;
        long parallelStart = System.currentTimeMillis();

        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
                parallelSum += sumThreads[i].getSum();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long parallelEnd = System.currentTimeMillis();
        long parallelTime = parallelEnd - parallelStart;

        int singleThreadSum = 0;
        long singleThreadStart = System.currentTimeMillis();
        for (int i = 0; i < arr.length; i++) {
            singleThreadSum += arr[i];
        }
        long singleThreadEnd = System.currentTimeMillis();
        long singleThreadTime = singleThreadEnd - singleThreadStart;

        System.out.println("Parallel sum: " + parallelSum + " | Time: " + parallelTime + " Millisecs");
        System.out.println("Single thread sum: " + singleThreadSum + " | Time: " + singleThreadTime + " Millisecs");
    }
}

class SumThread implements Runnable {
    private int start;
    private int end;
    private int[] arr;
    private int sum;

    public SumThread(int start, int end, int[] arr) {
        this.start = start;
        this.end = end;
        this.arr = arr;
        this.sum = 0;
    }

    public void run() {
        for (int i = start; i < end; i++) {
            sum += arr[i];
        }
    }

    public int getSum() {
        return sum;
    }
}

