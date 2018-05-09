import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        //We don't need to do anything here
        if (k == 0) {
            return;
        }
        RandomizedQueue<String> deque = new RandomizedQueue<>();

        while (!StdIn.isEmpty()) {
            deque.enqueue(StdIn.readString());
        }

        int i = 0;
        for (String s : deque) {
            StdOut.println(s);
            i++;
            if (i == k) {
                break;
            }
        }
    }
}
