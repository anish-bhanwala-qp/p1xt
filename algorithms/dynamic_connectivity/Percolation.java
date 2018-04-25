import java.util.*;

public class Percolation {
    public static void main(String args[]) {
       
    
        System.out.println(Arrays.toString(qf.vertices));
    }
    
    private final int BLOCKED = 0;
    private final int OPEN = 1;
    private int n;    
    private int grid[];
    private int firstRowRootIndex;
    private int lastRowRootIndex;
    
    // create n-by-n grid, with all sites blocked
   public Percolation(int n) {
        if (n ≤ 0) {
            throw new java.lang.IllegalArgumentException("n should be greater than 0");
        }
        
        this.n = n;
        //Element n*n is root connected connected to first row
        this.firstRowRootIndex = n*n;
        //Element n*n + 1 is root connected connected to last row
        this.lastRowRootIndex = n*n + 1;
        this.grid = int [n*n];
        for (int i=0; i < grid.length; i++) {
            grid[i] = BLOCKED;
        }
   }
   
   private isNumberOutOfRange(int val) {
       return (val < 1 || val > n);
   }
   
   //Col and row values are between 1 & n
   private void validate(int row, int col) {
       if (isNumberOutOfRange(row) || isNumberOutOfRange(col) ) {
           throw new java.lang.IllegalArgumentException("row & column should be between 1 and " + n);
       }
   }
   
   // open site (row, col) if it is not open already
   public void open(int row, int col) {    
       validate(row, col);
       
       //has left column
       if (col > 1) {
           if (isOpen(row, col - 1)) {
               connect(row, col, row, col - 1)
           }
       }
       //has right column
       if (col < n) {
           if (isOpen(row, col + 1)) {
               connect(row, col, row, col + 1)
           }
       }
       //has top column
       if (row > 1) {
           if (isOpen(row - 1, col)) {
               connect(row, col, row - 1, col)
           }
       }
       //has bottom column
       if (row < n) {
           if (isOpen(row + 1, col)) {
               connectRowCol(row, col, row + 1, col)
           }
       }
       
       if (row == 1) {
            connectToFirstRow(row, col);
       } else if (row == n) {
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
   
   //Returns index of row, col object in grid[] array
   //For 4x4 grid first element is 0, pos: 1,1 = 4*(1-1) + (1 - 1) 
   //For 4x4 grid first element if 2, pos: 1,3 = 4*(1-1) + (3 - 1) 
   //For 4x4 grid first element is 9, pos: 3,2 = 4*(3-1) + (2 - 1) 
   private int getGridIndexForRowCol(int row, int col) {
       return n*(row - 1) + (col - 1);
   }
   
   // is site (row, col) open?
   public boolean isOpen(int row, int col)  {
       validate(row, col);
       int gridIndex = getGridIndexForRowCol(row, col);
       return grid[gridIndex] == OPEN;
   }
   
   public boolean isFull(int row, int col) { 
       // is site (row, col) full?
       //throw java.lang.IllegalArgumentException
       validate(row, col);
   }
   public int numberOfOpenSites()       
   // number of open sites
   
   public boolean percolates()              
   // does the system percolate?

}

public class PercolationStats {
   public PercolationStats(int n, int trials)    
   //java.lang.IllegalArgumentException if either n ≤ 0 or trials ≤ 0.
   // perform trials independent experiments on an n-by-n grid
   public double mean()                          
   // sample mean of percolation threshold
   public double stddev()                        
   // sample standard deviation of percolation threshold
   public double confidenceLo()                  
   // low  endpoint of 95% confidence interval
   public double confidenceHi()                  
   // high endpoint of 95% confidence interval

   public static void main(String[] args)        
   // test client (described below)
}
