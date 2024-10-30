import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


// DRIVER FUNCTION 

public class TestingAVLTree {
    public static void main(String[] args) {
        AVLTree tree = new AVLTree();

        // Parse books from 'booklist.txt'
        BookParser parser = new BookParser();
        List<Book> books = parser.parseBooksFromFile("booklist.txt");

        // Insert each book into the AVL tree using the first 9 digits of ISBN as the key
        for (Book book : books) {
            int isbnKey = getISBNKey(book.getIsbn());
            tree.root = tree.insert(tree.root, isbnKey);
        }

        // Optionally, print the AVL tree in order or structure
        //tree.inOrder(tree.root);
        System.out.println("\n\n\nFinal Tree\n");
        tree.printTree(tree.root, "", true);
    }

    // Utility method to convert the first 9 digits of ISBN into an integer
    private static int getISBNKey(String isbn) {
        // Take the first 9 digits and convert them to an integer
        // This assumes ISBN is a 13-digit string
        if (isbn.length() >= 9) {
            return Integer.parseInt(isbn.substring(0, 9));
        } else {
            throw new IllegalArgumentException("Invalid ISBN: " + isbn);
        }
    }
}


class TreeNode {
	int key, height;
	TreeNode left, right;
	boolean lazyDeleted; // referenceVariables holding null or address of a TreeNode;

	public TreeNode(int k) {
		key = k;
		height = 0;
		// left = null; since left and right are instance ref. variables; not need ro
		// explicitly initialize
		// right = null;
	}
}

class AVLTree {
    TreeNode root; // default value null

    public TreeNode insert(TreeNode node, int key) { // recursive
        if (node == null)
            return (new TreeNode(key));

        // maintaining the bst order property
        if (key < node.key)
            node.left = insert(node.left, key);
        else if (key > node.key)
            node.right = insert(node.right, key);
        else // duplicates are not allowed
            return node;

        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));

        int balance = getBalance(node);

        // AVL logic for balancing

        // Left-Left (LL case)
        if (balance > 1 && key < node.left.key) {
            System.out.println("\n\n\nImbalance condition occurred at inserting ISBN " + key + "; fixed by Left-Left Rotation\n");
            TreeNode rotatedNode = rightRotate(node);
            printTree(root, "", true);  // Print the updated tree after rotation
            return rotatedNode;
        }

        // Right-Right (RR case)
        if (balance < -1 && key > node.right.key) {
            System.out.println("\n\n\nImbalance condition occurred at inserting ISBN " + key + "; fixed by Right-Right Rotation\n");
            TreeNode rotatedNode = leftRotate(node);
            printTree(root, "", true);  // Print the updated tree after rotation
            return rotatedNode;
        }

        // Left-Right (LR case)
        if (balance > 1 && key > node.left.key) {
            System.out.println("\n\n\nImbalance condition occurred at inserting ISBN " + key + "; fixed by Left-Right Rotation\n");
            node.left = leftRotate(node.left);
            TreeNode rotatedNode = rightRotate(node);
            printTree(root, "", true);  // Print the updated tree after rotation
            return rotatedNode;
        }

        // Right-Left (RL case)
        if (balance < -1 && key < node.right.key) {
            System.out.println("\n\n\nImbalance condition occurred at inserting ISBN " + key + "; fixed by Right-Left Rotation\n");
            node.right = rightRotate(node.right);
            TreeNode rotatedNode = leftRotate(node);
            printTree(root, "", true);  // Print the updated tree after rotation
            return rotatedNode;
        }

        return node;
    }

    public TreeNode leftRotate(TreeNode y) {
        TreeNode x = y.right;
        TreeNode T2 = x.left;

        // Perform rotation
        x.left = y;
        y.right = T2;

        // Update heights
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;

        return x;
    }

    public TreeNode rightRotate(TreeNode y) {
        TreeNode x = y.left;
        TreeNode T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        // Update heights
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;

        return x;
    }

    // Utility methods
    private int getHeight(TreeNode node) {
        if (node == null)
            return -1;

        return node.height;
    }

    private int getBalance(TreeNode node) {
        if (node == null)
            return 0;
        return getHeight(node.left) - getHeight(node.right);
    }

    // Method to print the structure of the tree
    public void printTree(TreeNode node, String prefix, boolean isLeft) {
        if (node != null) {
            System.out.printf("%s%s%d (b: %d, h: %d)\n", prefix, (isLeft ? "L── " : "R──"), node.key, getBalance(node),
                    getHeight(node));
            printTree(node.left, prefix + (isLeft ? "|  " : "  "), true);
            printTree(node.right, prefix + (isLeft ? "|  " : "  "), false);
        }
    }

    public void inOrder(TreeNode node) {
        if (node != null) {
            inOrder(node.left);
            System.out.print(node.key + " ");
            inOrder(node.right);
        }
    }
}



class Book {
    private String isbn;
    private String title;
    private String authors;

    // Constructor
    public Book(String isbn, String title, String authors) {
        this.isbn = isbn;
        this.title = title;
        this.authors = authors;
    }

    // Getters for the fields
    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    @Override
    public String toString() {
        return "ISBN: " + isbn + "\nTitle: " + title + "\nAuthors: " + authors + "\n";
    }
}


class BookParser {

    // Method to parse books from a text file
    public List<Book> parseBooksFromFile(String filePath) {
        List<Book> books = new ArrayList<>();

        try {
            // Read the file
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);

            // Variables to hold book details temporarily
            String isbn = null;
            String title = null;
            String authors = null;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                // Check if the line is an ISBN (by length or number pattern)
                if (line.matches("\\d{13}")) {
                    // If there is already a book being processed, add it to the list
                    if (isbn != null) {
                        books.add(new Book(isbn, title, authors));
                    }

                    // Start a new book entry
                    isbn = line;
                    title = null;
                    authors = null;

                } else if (title == null) {
                    // The first non-ISBN line is the title
                    title = line;

                } else {
                    // Remaining lines are authors (append them if multiple lines)
                    if (authors == null) {
                        authors = line;
                    } else {
                        authors += " - " + line;
                    }
                }
            }

            // Add the last book entry after the loop
            if (isbn != null) {
                books.add(new Book(isbn, title, authors));
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
            e.printStackTrace();
        }

        return books;
    }
}