import java.util.*;

public class Homework5_SplayTree {
    public static void main(String[] args) {
        int key;
        Scanner input = new Scanner(System.in);
        SplayTree<Integer, Integer> tree = new SplayTree<>();
        
        while (true) {
            System.out.print("Enter a key (-1 to exit): ");
            key = input.nextInt();
            if (key < 0) break;
            tree.root = tree.insert(tree.root, key, key);
            tree.printTree(tree.root, "", false);
        }
        
        tree.printTree(tree.root, "", false);
    }
}

class SplayTree<Key extends Comparable<Key>, Value> {
    
    private class TreeNode {
        Key key;
        Value value;
        TreeNode left, right;
        
        TreeNode(Key key, Value value) {
            this.key = key;
            this.value = value;
        }
    }
    
    TreeNode root;
    public SplayTree() {
        root = null;
    }
    
    public TreeNode insert(TreeNode node, Key key, Value value) {
        if (node == null) {
            return (new TreeNode(key, value));
        }
        
        if (key.compareTo(node.key) < 0) {
            node.left = insert(node.left, key, value);  // Correctly updating node.left
        }
        else if (key.compareTo(node.key) > 0) {
            node.right = insert(node.right, key, value);  // Correctly updating node.right
        }
        else 
            return node;
            
        return node;
    }
    
    public void printTree(TreeNode node, String prefix, boolean isLeft) {
        if (node != null) {
            System.out.printf("%s%s%d\n", prefix, (isLeft ? "L── " : "R──"), node.key);
            printTree(node.left, prefix + (isLeft ? "|  " : "  "), true);
            printTree(node.right, prefix + (isLeft ? "|  " : "  "), false);
        }
    }
}
