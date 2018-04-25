import java.util.*;

/*
Main difference between QU & WQU is to reduce the height of tree. 
We achieve that by always making smaller tree child of large tree root node.
In this case the height of tree increases onlyif the size of the two trees
are equal. We can double the size of a tree only logN times, hence complexity
in this case is logN
*/
public class WeightedQuickUnion {
    public static void main(String args[]) {
        WeightedQuickUnion qf = new WeightedQuickUnion(10);
        
        qf.union(0, 1);
        qf.union(0, 2);
        qf.union(3, 4);
        qf.union(4, 5);
        qf.union(2, 5);
        
        System.out.println(String.format("1, 3 is connected: %b", qf.find(1, 3)));
        System.out.println(String.format("2, 4 is connected: %b", qf.find(2, 4)));
        System.out.println(String.format("1, 8 is connected: %b", qf.find(1, 8)));
        
        System.out.println(Arrays.toString(qf.vertices));
    }
    
    int vertices[];
    int size[];
    
    WeightedQuickUnion(int N) {
        this.vertices = new int[N];
        this.size = new int[N];
        for (int i=0; i < N; i++) {
            this.vertices[i] = i;
            this.size[i] = 1;
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
        System.out.println(String.format("Union a: %d, b: %d", a, b));
        int a_root = root(a);
        int b_root = root(b);
        
        if (a_root == b_root) {
            return;
        }
        
        //b tree is smaller, Make it b child of tree a
        if (size[a_root] > size[b_root]) {
            vertices[b_root] = a_root; 
            size[a] += size[b];
        } else {
            //As a tree is smaller, make a tree child of b tree
            vertices[a_root] = b_root;
            size[b] += size[a];
        }
        System.out.println(String.format("Union result: %s", Arrays.toString(vertices)));
    }
}
