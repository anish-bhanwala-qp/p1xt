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

import java.util.Arrays;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {    
    private static final int BLOCKED = 0;
    private static final int OPEN = 1;

    private int n;     
    private int[] grid;
    
    private int firstRowRootIndex;
    private int lastRowRootIndex;
    private int openSiteCount;
    private WeightedQuickUnionUF algo;
   
     //  create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException("n should be greater than 0");
        }

        this.openSiteCount = 0;
        this.n = n;
        // it includes two additional root nodes
        // one connecting top row and other connection bottom row
        int gridSize = n*n + 2; 
        this.grid = new int[gridSize];        
        
        for (int i = 0; i < grid.length; i++) {
            grid[i] = BLOCKED;            
        }

        algo = new WeightedQuickUnionUF(gridSize);

          // Element n*n is root connected connected to first row
        this.firstRowRootIndex = gridSize - 2;
        grid[firstRowRootIndex] = OPEN;
        
          // Element n*n + 1 is root connected connected to last row
        this.lastRowRootIndex = gridSize - 1;
        grid[lastRowRootIndex] = OPEN;        
    }

    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        int totalTries = 0;

        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                totalTries++;
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                System.out.println("row: " + row + ", col: " + col);
                p.open(row, col);
                p.print();
            }

            System.out.println("TotalTries: " + totalTries + ", for iteration: " + i);
        }
    }

    public int getOpenSiteCount() {
        return this.openSiteCount;
    }

    private void print() {
        System.out.println("grid: " + Arrays.toString(grid));        
    }

    private boolean isNumberOutOfRange(int val) {
        return (val < 1 || val > n);
    }

     // Col and row values are between 1 & n
    private void validate(int row, int col) {
        if (isNumberOutOfRange(row) || isNumberOutOfRange(col)) {
            throw new java.lang.IllegalArgumentException("row & column should be between 1 and " + n);
        }
    }

     //  open site (row, col) if it is not open already
    public void open(int row, int col) {     
        validate(row, col);

        int gridIndex = getGridIndexForRowCol(row, col);
        if (grid[gridIndex] == OPEN) {
         // already open, do nothing
            return;
        }

        grid[gridIndex] = OPEN;
        this.openSiteCount++;

        // has left column
        if (col > 1) {
            if (isOpen(row, col - 1)) {
                connectRowCol(row, col, row, col - 1);
            }
        }
        // has right column
        if (col < n) {
            if (isOpen(row, col + 1)) {
                connectRowCol(row, col, row, col + 1);
            }
        }
        // has top column
        if (row > 1) {
            if (isOpen(row - 1, col)) {
                connectRowCol(row, col, row - 1, col);
            }
        }
        // has bottom column
        if (row < n) {
            if (isOpen(row + 1, col)) {
                connectRowCol(row, col, row + 1, col);
            }
        }

        if (row == 1) {
            connectToFirstRow(row, col);
        } 
        else if (row == n) {
            connectToLastRow(row, col);
        }
    }

    private void connectRowCol(int row, int col, int adjacentRow, int adjacentCol) {
        connect(getGridIndexForRowCol(row, col), getGridIndexForRowCol(adjacentRow, adjacentCol));
    }

    private void connectToFirstRow(int row, int col) {
        connect(getGridIndexForRowCol(row, col), firstRowRootIndex);
    }

    private void connectToLastRow(int row, int col) {
        connect(getGridIndexForRowCol(row, col), lastRowRootIndex);
    }

    private void connect(int index1, int index2) {
        algo.union(index1, index2);
    }

    private boolean areConnected(int index1, int index2) {
        return algo.connected(index1, index2);
    }

     // Returns index of row, col object in grid[] array
     // For 4x4 grid first element is 0, pos: 1,1 = 4*(1-1) + (1 - 1) 
     // For 4x4 grid first element if 2, pos: 1,3 = 4*(1-1) + (3 - 1) 
     // For 4x4 grid first element is 9, pos: 3,2 = 4*(3-1) + (2 - 1) 
    private int getGridIndexForRowCol(int row, int col) {
        return n*(row - 1) + (col - 1);
    }

     //  is site (row, col) open?
    public boolean isOpen(int row, int col)  {
        validate(row, col);
        int gridIndex = getGridIndexForRowCol(row, col);
        return grid[gridIndex] == OPEN;
    }

     //  is site (row, col) full?
    public boolean isFull(int row, int col) { 
        validate(row, col);
        int gridIndex = getGridIndexForRowCol(row, col);
        return areConnected(gridIndex, firstRowRootIndex);
    }

     //  number of open sites
    public int numberOfOpenSites() {        
        return openSiteCount;
    }

     //  does the system percolate?
    public boolean percolates() {
    // when firtRow root and lastRow root have same value..i.e. they are connected
        return areConnected(firstRowRootIndex, lastRowRootIndex);
    }        
}
