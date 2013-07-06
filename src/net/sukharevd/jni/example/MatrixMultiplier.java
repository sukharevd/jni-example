package net.sukharevd.jni.example;

import java.io.File;

public class MatrixMultiplier {

    public static void main(String[] args) throws InterruptedException {
        File sharedLibrary
            = new File("net_sukharevd_jni_example_MatrixMultiplier.so");
        System.load(sharedLibrary.getAbsolutePath());

        // Warming up JVM
        Thread.sleep(10000);
        System.out.println("Estimation output:");
        printTimeMeasurementsFor(500);

        // if (compareResults(500)) System.out.println("Results are the same");
        // else System.err.println("Error! Different results!");

        System.out.println("N\tJNI\tPure Java");
        for (int n = 250; n <= 4000; n += 250) {
            printTimeMeasurementsFor(n);
        }
    }
    
    private native int[][] multiply(int[][] a, int[][] b);

    private int[][] pureJavaMultiply(int[][] a, int[][] b) {
        int[][] res = new int[a.length][a.length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                res[i][j] = 0;
                for (int k = 0; k < a.length; k++) {
                    res[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return res;
    }

    public long measureJni(int[][] a, int[][] b) {
        long tbegin = System.currentTimeMillis();
        multiply(a, b);
        return System.currentTimeMillis() - tbegin;
    }

    public long measurePureJava(int[][] a, int[][] b) {
        long tbegin = System.currentTimeMillis();
        pureJavaMultiply(a, b);
        return System.currentTimeMillis() - tbegin;
    }

    private static void printTimeMeasurementsFor(int n) {
        MatrixMultiplier c = new MatrixMultiplier();

        // Generate sample data
        int[][] a = new int[n][n];
        int[][] b = new int[n][n];
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b.length; j++) {
                a[i][j] = (i % 10) * 10 + j;
                b[i][j] = 2 + j;
            }
        }

        // Benchmark
        final int ATTEMPS = 50;
        long tJni = 0, tPureJava = 0;
        for (int i = 0; i < ATTEMPS; i++) {
            tJni += c.measureJni(a, b);
            tPureJava += c.measurePureJava(a, b);
        }

        System.out.println(n + "\t" + (tJni/ATTEMPS) + "\t" + (tPureJava/ATTEMPS));
    }
    
    private static boolean compareResults(int n) {
        MatrixMultiplier c = new MatrixMultiplier();
        // Generate sample data
        int[][] a1 = new int[n][n];
        int[][] b1 = new int[n][n];
        for (int i = 0; i < b1.length; i++) {
            for (int j = 0; j < b1.length; j++) {
                a1[i][j] = (i % 10) * 10 + j;
                b1[i][j] = 2 + j;
            }
        }
        int[][] res1 = c.multiply(a1, b1);
        int[][] res2 = c.pureJavaMultiply(a1, b1);
        for (int i = 0; i < res1.length; i++) {
            for (int j = 0; j < res1[i].length; j++) {
                if (res1[i][j] != res2[i][j]) {
                    System.err.println("Diff: [" + i + "," + j + "]: "
                            + res1[i][j] + " vs. " + res2[i][j]);
                    return false;
                }
            }
        }
        return true;
    }
}
