import java.util.*;

public class Percolation {
    public static void main(String args[]) {
       Percolation p = new Percolation(4);
       
       System.out.println("percolates: " + p.percolates());
       
       p.print();
       p.open(1,1);
       System.out.println("1,1 percolates: " + p.percolates());
       
       p.print();
       p.open(2,1);
       System.out.println("2,1 percolates: " + p.percolates());
       
       p.print();
       p.open(3,1);
       
       System.out.println("3,1 percolates: " + p.percolates());
       p.print();
       p.open(4,1);
       
       System.out.println("4,1 percolates: " + p.percolates());
       p.print();
    }
    
    private final int BLOCKED = 0;
    private final int OPEN = 1;
    private int n;    
    private int grid[];
    private int connections[];
    private int size[];
    private int firstRowRootIndex;
    private int lastRowRootIndex;
    private int openSiteCount;
    
    // create n-by-n grid, with all sites blocked
   public Percolation(int n) {
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException("n should be greater than 0");
        }
        
        this.openSiteCount = 0;
        this.n = n;
        this.grid = new int[n*n + 2];
        this.connections = new int[n*n + 2];
        this.size = new int[n*n + 2];
        for (int i=0; i < grid.length; i++) {
            grid[i] = BLOCKED;
            connections[i] = i;
            size[i] = 1;
        }
        
        //Element n*n is root connected connected to first row
        this.firstRowRootIndex = n*n;
        grid[firstRowRootIndex] = OPEN;
        size[firstRowRootIndex] = 1;
        connections[firstRowRootIndex] = firstRowRootIndex;
        //Element n*n + 1 is root connected connected to last row
        this.lastRowRootIndex = n*n + 1;
        grid[lastRowRootIndex] = OPEN;
        connections[lastRowRootIndex] = lastRowRootIndex;
        size[lastRowRootIndex] = 1;
   }
   
   private void print() {
       System.out.println("grid: " + Arrays.toString(grid));
       System.out.println("size: " + Arrays.toString(size));
       System.out.println("connection: " + Arrays.toString(connections));
   }
   
   private boolean isNumberOutOfRange(int val) {
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
       
       int gridIndex = getGridIndexForRowCol(row, col);
       if (grid[gridIndex] == OPEN) {
           //already open, do nothing
           return;
       }
       
       grid[gridIndex] = OPEN;
       this.openSiteCount++;
       
       //has left column
       if (col > 1) {
           if (isOpen(row, col - 1)) {
               connectRowCol(row, col, row, col - 1);
           }
       }
       //has right column
       if (col < n) {
           if (isOpen(row, col + 1)) {
               connectRowCol(row, col, row, col + 1);
           }
       }
       //has top column
       if (row > 1) {
           if (isOpen(row - 1, col)) {
               connectRowCol(row, col, row - 1, col);
           }
       }
       //has bottom column
       if (row < n) {
           if (isOpen(row + 1, col)) {
               connectRowCol(row, col, row + 1, col);
           }
       }
       
       if (row == 1) {
            connectToFirstRow(row, col);
       } else if (row == n) {
            connectToLastRow(row, col);
       }
   }
   
   private int root(int index) {
       int rootIndex = connections[index];
       while (rootIndex != connections[rootIndex]) {
           rootIndex = connections[rootIndex];
       }
       
       return rootIndex;
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
       int rootIndex1 = root(index1);
       int rootIndex2 = root(index2);
       if (root(index1) == root(index2)) {
           //do nothing already connected
           return;
       }
       
       if (size[index1] > size[index2]) {
           connections[rootIndex2] = rootIndex1;
           size[index1] += size[index2];
       } else {
           connections[rootIndex1] = rootIndex2;
           size[index2] += size[index1];
       }
   }
   
   private boolean areConnected(int index1, int index2) {
       return root(index1) == root(index2);
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
   
   // is site (row, col) full?
   public boolean isFull(int row, int col) { 
       validate(row, col);
       int gridIndex = getGridIndexForRowCol(row, col);
       return areConnected(gridIndex, firstRowRootIndex);
   }
   
   // number of open sites
   public int numberOfOpenSites() {      
        return openSiteCount;
   }
   
   // does the system percolate?
   public boolean percolates() {
       //when firtRow root and lastRow root have same value..i.e. they are connected
       return connections[firstRowRootIndex] == connections[lastRowRootIndex];
   }             
}

// public class PercolationStats {
//   public PercolationStats(int n, int trials)    
//   //java.lang.IllegalArgumentException if either n ≤ 0 or trials ≤ 0.
//   // perform trials independent experiments on an n-by-n grid
//   public double mean()                          
//   // sample mean of percolation threshold
//   public double stddev()                        
//   // sample standard deviation of percolation threshold
//   public double confidenceLo()                  
//   // low  endpoint of 95% confidence interval
//   public double confidenceHi()                  
//   // high endpoint of 95% confidence interval

//   public static void main(String[] args)        
//   // test client (described below)
// }
