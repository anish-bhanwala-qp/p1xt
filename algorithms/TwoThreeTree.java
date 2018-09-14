public class TwoThreeTree<T extends Comparable> {
    // How to represent two node?
    // data, left link , right link
    // How to represent 3 node?
    // data1, data2, left link, middle link, right link
    // How to represent 4 node?
    // data1, data2, data3, 4 links

    private Node<T> root;

    public void add(T data) {
        root = add(root, data);
    }

    public void traverse() {
        traverse(root);
        System.out.println();
    }

    private void traverse(Node<T> node) {
        if (node == null)
            return;

        traverse(node.left);

        if (node.isThreeNode()) {
            T min = node.data1.compareTo(node.data2) < 0 ? node.data1 : node.data2;
            T max = min == node.data1 ? node.data2 : node.data1;
            System.out.print(min + " ");
            traverse(node.middle);  
            System.out.print(max + " ");
        } else {
            System.out.print(node.data1 + " ");
        }

        traverse(node.right);
    }

    private Node<T> add(Node<T> node, T data) {
        if (node == null) {
            node = new Node<>();
            node.data1 = data;
            return node;
        }

        // Do nothing if the data is matching ro current values
        if (node.data1.compareTo(data) == 0)
            return node;
        if (node.data2 != null && data.compareTo(node.data2) == 0)
            return node;

        if (node.isLeafNode()) {
            return addToLeafNode(node, data);
        }

        // go to left if 3 node and data is less than data1 & data2
        // or 2 node and data is less than data 1
        if ((node.isThreeNode() && data.compareTo(node.data1) < 0 && data.compareTo(node.data2) < 0)
                || !node.isThreeNode() && data.compareTo(node.data1) < 0) {
            // go left
            Node<T> left = add(node.left, data);
            if (!left.isTemp3Node) {
                node.left = left;
                return node;
            } else if (node.isThreeNode()) {
                return child4NodeAndParent3Node(node, left);
            } else {
                return child4NodeAndParent2Node(node, left);
            }
        }
        // go middle if 3 node and data between data1 and data2
        // not possible with 2 node, there is not middle
        else if (node.isThreeNode() && data.compareTo(node.data1) > 0 && data.compareTo(node.data2) < 0) {
            Node<T> middle = add(node.middle, data);
            if (!middle.isTemp3Node) {
                node.middle = middle;
                return node;
            } else {
                return child4NodeAndParent3Node(node, middle);
            }
        }
        // go right if 3 node and data is greater than data 1 & data 2
        // or 2 node and data is greater than data1
        else if ((node.isThreeNode() && data.compareTo(node.data1) > 0 && data.compareTo(node.data2) > 0)
                || !node.isThreeNode() && data.compareTo(node.data1) > 0) {
            // go right
            Node<T> right = add(node.right, data);
            if (!right.isTemp3Node) {
                node.right = right;
                return node;
            } else if (node.isThreeNode()) {
                return child4NodeAndParent3Node(node, right);
            } else {
                return child4NodeAndParent2Node(node, right);
            }
        } else {
            throw new IllegalStateException("This should not hapen!");
        }
    }

    // Adding data to leaf 3 node, left child
    Node<T> addToLeafNode(Node<T> n, T data) {
        if (!n.isThreeNode()) {
            n.data2 = data;
            return n;
        }

        T min = min(n, data);
        T max = max(n, data);
        T middle = middle(n, data);
        // TODO: Not considering equal elements case here

        // System.out.printf("%d, %d, %d\n", min, middle, max);

        Node<T> left = new Node<>();
        left.data1 = min;

        Node<T> right = new Node<>();
        right.data1 = max;

        Node<T> parent = n;
        parent.data1 = middle;
        parent.data2 = null;
        parent.left = left;
        parent.right = right;
        parent.isTemp3Node = true;

        return parent;
    }

    // Handle parent when child becomes 4 node, and parent is 3 node
    Node<T> child4NodeAndParent3Node(Node<T> parent, Node<T> child) {
        child.isTemp3Node = false;

        assert parent.data1.compareTo(child.right.data1) > 0 : "parent data always be > then children";

        T min = parent.data1.compareTo(parent.data2) < 0 ? parent.data1 : parent.data2;
        T max = min == parent.data1 ? parent.data2 : parent.data1;

        // Its right child
        if (child.data1.compareTo(max) > 0) {
            Node<T> nLeft = new Node<>();
            nLeft.data1 = min;
            nLeft.left = parent.left;
            nLeft.right = parent.middle;

            parent.data1 = max;
            parent.left = nLeft;
            parent.middle = null;
            parent.right = child;

            return parent;
        }
        // Its left child
        else if (child.data1.compareTo(min) < 0) {
            Node<T> nRight = new Node<>();
            nRight.data1 = max;
            nRight.left = parent.middle;
            nRight.right = parent.right;

            parent.data1 = min;
            parent.left = child;
            parent.middle = null;
            parent.right = nRight;

            return parent;
        }
        // Its middle child
        else {
            Node<T> nLeft = new Node<>();
            nLeft.data1 = min;
            nLeft.left = parent.left;
            nLeft.right = child.left;

            Node<T> nRight = new Node<>();
            nRight.data1 = max;
            nRight.left = child.right;
            nRight.right = parent.right;

            parent.data1 = child.data1;
            parent.left = nLeft;
            parent.middle = null;
            parent.right = nRight;

            child.left = null;
            child.right = null;

            return parent;
        }
    }

    // Handle parent when child becomes 4 node, and parent is 2 node
    Node<T> child4NodeAndParent2Node(Node<T> parent, Node<T> child) {
        child.isTemp3Node = false;

        assert parent.data1.compareTo(child.right.data1) > 0 : "parent data always be > then children";

        // Its right child
        if (child.data1.compareTo(parent.data1) > 0) {
            // child.right becomes middle
            parent.middle = child.left;
            // child.left becomes left
            parent.right = child.right;
            // add child.data1 to the parent
            parent.data2 = child.data1;

            // remove child node
            child.left = null;
            child.right = null;
            child.data1 = null;
            child.data2 = null;

            return parent;
        }
        // Its left child
        else {
            // child.right becomes middle
            parent.middle = child.right;
            // child.left becomes left
            parent.left = child.left;
            // add child.data1 to the parent
            parent.data2 = child.data1;

            // remove child node
            child.left = null;
            child.right = null;
            child.data1 = null;
            child.data2 = null;
            
            return parent;
        }
    }

    T min(Node<T> n, T data) {
        if (data.compareTo(n.data1) < 0) {
            return (data.compareTo(n.data2) < 0) ? data : n.data2;
        } else if (n.data1.compareTo(n.data2) < 0) {
            return n.data1;
        } else {
            return n.data2;
        }
    }

    T middle(Node<T> n, T data) {
        if (data.compareTo(n.data1) < 0) {
            if (data.compareTo(n.data2) < 0) {
                return (n.data1.compareTo(n.data2) < 0) ? n.data1 : n.data2;
            } else {
                return (n.data1.compareTo(data) < 0) ? n.data1 : data;
            }
        } else if (n.data1.compareTo(n.data2) < 0) {
            return (n.data2.compareTo(data) < 0) ? n.data2 : data;
        } else {
            return (n.data1.compareTo(data) < 0) ? n.data1 : data;
        }
    }

    T max(Node<T> n, T data) {
        if (data.compareTo(n.data1) < 0) {
            if (data.compareTo(n.data2) < 0) {
                return (n.data1.compareTo(n.data2) < 0) ? n.data2 : n.data1;
            } else {
                return (n.data1.compareTo(data) < 0) ? data : n.data1;
            }
        } else if (n.data1.compareTo(n.data2) < 0) {
            return (n.data2.compareTo(data) < 0) ? data : n.data2;
        } else {
            return (n.data1.compareTo(data) < 0) ? data : n.data1;
        }
    }

    private static class Node<T> {
        T data1;
        T data2;
        Node<T> left;
        Node<T> middle;
        Node<T> right;
        boolean isTemp3Node;

        boolean isThreeNode() {
            return data1 != null && data2 != null;
        }

        boolean isLeafNode() {
            return data1 == null && data2 == null;
        }
    }

    public static void main(String[] args) {
        TwoThreeTree<Integer> tree = new TwoThreeTree<>();
        for (int i = 1; i < 10; i++) {
            tree.add(i);
        }
        tree.traverse();

        TwoThreeTree<Integer> tree2 = new TwoThreeTree<>();
        for (int i = 10; i > 0; i--) {
            tree2.add(i);
        }
        tree2.traverse();

        TwoThreeTree<Integer> tree3 = new TwoThreeTree<>();
        tree3.add(3);
        tree3.add(1);
        tree3.add(5);
        tree3.add(2);
        tree3.add(4);
        tree3.add(6);

        tree3.traverse();
    }
}
