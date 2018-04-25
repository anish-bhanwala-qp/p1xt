import java.util.*;

public class QuickUnion {
    public static void main(String args[]) {
        QuickUnion qf = new QuickUnion(10);
        
        qf.union(0, 1);
        qf.union(0, 2);
        qf.union(1, 3);
        qf.union(2, 8);
        
        System.out.println(String.format("1, 3 is connected: %b", qf.find(1, 3)));
        System.out.println(String.format("2, 4 is connected: %b", qf.find(2, 4)));
        System.out.println(String.format("1, 8 is connected: %b", qf.find(1, 8)));
        
        System.out.println(Arrays.toString(qf.vertices));
    }
    
    int vertices[];
    
    QuickUnion(int N) {
        this.vertices = new int[N];
        for (int i=0; i < N; i++) {
            this.vertices[i] = i;
        }
    }
    
    int root(int a) {
        int root_index = vertices[a];
        while (vertices[root_index] != root_index) {
            root_index = vertices[root_index];
        } 
        
        return root_index;
    }
    
    //Find if two vertices are connected
    boolean find(int a, int b) {
        return root(a) == root(b);
    }
    
    //Connect two vertices
    void union(int a, int b) {
        int a_root = root(a);
        int b_root = root(b);
        
        if (a_root == b_root) {
            return;
        }
        
        //Make b tree child of a tree
        vertices[b_root] = a_root;
    }
}
