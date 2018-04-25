/******************************************************************************
 *  Name:    Anish Bhanwala
 *  NetID:   anish
 *  Precept: P01
 *
 *  Partner Name:    N/A
 *  Partner NetID:   N/A
 *  Partner Precept: N/A
 * 
 *  Description:  Perlocation problem solution.
 ******************************************************************************/

import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    private double[] totalTries;    

    private double meanResult;
    private double stdDevResult;
    private double confidenceLoResult;
    private double confidenceHiResult;

    //  perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        validate(n);        
        double totalGridElements = (n*n);

        this.totalTries = new double[trials];

        int row, col, triesCount = 0;
        
        for (int i = 0; i < trials; i++) {            
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                triesCount++;
                row = StdRandom.uniform(n) + 1;
                col = StdRandom.uniform(n) + 1;                
                p.open(row, col);                
            }
            // System.out.println("getOpenSiteCount: " + p.getOpenSiteCount());
            // System.out.println("triesCount: " + triesCount);

            this.totalTries[i] = p.getOpenSiteCount()/totalGridElements;
        }        

        this.meanResult = StdStats.mean(totalTries);
        this.stdDevResult = StdStats.stddev(totalTries);

        this.confidenceLoResult = meanResult - ((1.96*stdDevResult)/Math.sqrt(trials));
        this.confidenceHiResult = meanResult + ((1.96*stdDevResult)/Math.sqrt(trials));
    }
         
    private void validate(int val) {
        if (val <= 0) {
            throw new java.lang.IllegalArgumentException("Value must be greater than 0");
        }
    }

    public double mean() {
        return this.meanResult;
    }

    public double stddev() {
        return this.stdDevResult;
    }
    
    public double confidenceLo() {
        return this.confidenceLoResult;
    }

    public double confidenceHi() {
        return this.confidenceHiResult;
    }

    private void print() {
        System.out.println("mean: " + meanResult + 
            "\n%stdev: " + stdDevResult + 
            "\nconfLo: " + confidenceLoResult + 
            "\n, confHi: " + confidenceHiResult + "\n");
    }

    public static void main(String[] args) {         
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(n, trials);
        ps.print();
    }
}
