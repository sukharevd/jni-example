package com.example;

import java.io.File;

public class Calculator {

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
    
    private void printMatrix(int[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                System.out.print(a[i][j] + "\t");
            }
            System.out.println();
        }
    }
    
    public long testJni(int[][] a, int[][] b) {
        long tbegin = System.currentTimeMillis();
        multiply(a, b);
        long tend = System.currentTimeMillis();
        return tend - tbegin;
    }
    
    public long testPureJava(int[][] a, int[][] b) {
        long tbegin = System.currentTimeMillis();
        int[][] res = pureJavaMultiply(a, b);
        int c = res[0][0];                    // it's always 0
        c -= c;                               // attempt to avoid optimization
                                              // as far as res is used nowhere
        long tend = System.currentTimeMillis();
        return tend - tbegin + c;
    }
    
    public static void main(String[] args) throws InterruptedException {
    	System.load(new File("com_example_Calculator.so").getAbsolutePath());
        
    	System.out.println("Estimation output:");
    	
//    	// Generate sample data
//        int[][] a1 = new int[500][500];
//        int[][] b1 = new int[500][500];
//        for (int i = 0; i < b1.length; i++) {
//            for (int j = 0; j < b1.length; j++) {
//                a1[i][j] = (i%10) * 10 + j;
//                b1[i][j] = 2 + j;
//            }
//        }
        
        // Warming up JVM
        Thread.sleep(10000);
        printTimeMeasurementsFor(500);
        
//        int[][] res1 = c.multiply(a, b);
//        int[][] res2 = c.testMultiply(a, b);
//        boolean same = true;
//        for (int i = 0; i < res1.length; i++) {
//			for (int j = 0; j < res1[i].length; j++) {
//				if (res1[i][j] != res2[i][j]) {
//					System.err.println("Diff: [" + i +","+j+"]: "+res1[i][j]+" vs. "+res2[i][j]);
//					same = false;
//				}
//			}
//		}
//        if (same) System.out.println("Result is the same");
//        else System.err.println("Error! Different results!");
        
        //c.testJni(a1, b1);
        //c.testPureJava(a1, b1);
        System.out.println("N\tJNI\tPure Java");
    	for (int n = 250; n <= 4000; n+=250) {
    		printTimeMeasurementsFor(n);
    	}
        
        //int[][] res = c.multiply(a, b);
        //System.out.println("JNI Result:\n");
        //c.printMatrix(c.multiply(a, b));
        //System.out.println("Java Result:\n");
        //c.printMatrix(c.testMultiply(a, b));
    }

	private static void printTimeMeasurementsFor(int n) {
		Calculator c = new Calculator();
      //int n = 1000;
      // Generate sample data
      int[][] a = new int[n][n];
      int[][] b = new int[n][n];
      for (int i = 0; i < b.length; i++) {
		for (int j = 0; j < b.length; j++) {
		    a[i][j] = (i%10) * 10 + j;
		    b[i][j] = 2 + j;
		}
      }
      
      // Benchmark
      long tJni = 0, tPureJava = 0;
      for (int i = 0; i < 50; i++) {
		tJni += c.testJni(a, b);
		tPureJava += c.testPureJava(a, b);
      }
      
      System.out.println(n + "\t" + tJni/50 + "\t" + tPureJava/50);
	}
}
