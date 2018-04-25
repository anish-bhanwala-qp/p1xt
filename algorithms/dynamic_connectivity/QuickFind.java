public class QuickFind {
    public static void main(String args[]) {
        QuickFind qf = new QuickFind(10);
        
        qf.union(0, 1);
        qf.union(0, 2);
        qf.union(1, 3);
        
        System.out.println(String.format("1, 3 is connected: %b", qf.find(1, 3))); //True
        System.out.println(String.format("2, 4 is connected: %b", qf.find(2, 4))); //False
    }
    
    int vertices[];
    
    QuickFind(int N) {
        this.vertices = new int[N];
        for (int i=0; i < N; i++) {
            this.vertices[i] = i;
        }
    }
    
    //Find if two vertices are connected
    boolean find(int a, int b) {
        return vertices[a] == vertices[b];
    }
    
    //Connect two vertices
    void union(int a, int b) {
        int a_root = vertices[a];
        int b_root = vertices[b];
        
        //already connected
        if (a_root == b_root) {
            return;
        }
        
        for (int i=0; i < vertices.length; i++) {
            if (vertices[i] == b_root) {
                vertices[i] = a_root;
            }    
        }
    }
}
