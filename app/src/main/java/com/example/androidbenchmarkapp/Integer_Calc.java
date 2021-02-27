package com.example.androidbenchmarkapp;

import java.util.Arrays;

public class Integer_Calc {

    public double start, finish, elapsedTime;

    protected int[] findPrimes(int max) {
        int[] primes = new int[max];
        int found = 0;
        boolean isPrime;

        // initial prime
        if (max > 2) {
            primes[found++] = 2;

            for (int x = 3; x <= max; x += 2) {
                isPrime = true; // prove it's not prime
                for (int i = 0; i < found; i++) {
                    isPrime = x % primes[i] != 0; // x is not prime if it is
                    // divisible by p[i]
                    if (!isPrime || primes[i] * primes[i] > x) {
                        break;
                    }
                }
                if (isPrime) {
                    primes[found++] = x; // add x to our prime numbers
                }
            }
        }

        return Arrays.copyOf(primes, found);
    }

    protected int InputPrime(String index) {

        int MAX_N = Integer.MAX_VALUE / 100;
        int n = 0;
        while (n <= 0 || n >= MAX_N) {
            n = Integer.parseInt(index);
            if (n <= 0) {
                //System.out.println("n must be greater than 0");
            }
            if (n >= MAX_N) {
                //System.out.println("n must be smaller than " + MAX_N);
            }
        }
        int max = n * 100; // just find enough prime numbers....

        start = System.nanoTime();
        int[] primes = findPrimes(max);
        finish = System.nanoTime();

        elapsedTime = (finish - start) / 1000000;

        return primes[n-1];
    }

}
