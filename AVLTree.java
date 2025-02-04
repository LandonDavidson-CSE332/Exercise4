import java.util.Stack;

public class AVLTree  <K extends Comparable<K>, V> extends BinarySearchTree<K,V> {

    public AVLTree(){
        super();
    }

    public V insert(K key, V value) {
        Stack<TreeNode<K, V>> stack = new Stack<>();
        // Search through tree to find insert position while adding parents to the stack
        TreeNode<K, V> cur_node = root;
        while(cur_node.left != null || cur_node.right != null) {
            stack.add(cur_node);
            // If the key is in the list then replace value and return the old one
            if (cur_node.key == key) {
                V old_val = cur_node.value;
                cur_node.value = value;
                return old_val;
            }
            // Traverse the tree, left if key is smaller and right if key is larger
            if (cur_node.key.compareTo(key) < 0) {
                // If there is an empty spot where the new node should go
                // create it with no children so we leave the loop
                if (cur_node.right == null) {
                    cur_node.right = new TreeNode<>(key, value);
                }
                cur_node = cur_node.right;
            } else {
                // If there is an empty spot where the new node should go
                // create it with no children so we leave the loop
                if (cur_node.left == null) {
                    cur_node.left = new TreeNode<>(key, value);
                }
                cur_node = cur_node.left;
            }
        }
        // Update the heights and rotate each node in the stack (each parent of inserted node)
        while (!stack.isEmpty()) {
            // Pop current nodes parent from stack and update height
            cur_node = stack.pop();
            cur_node.updateHeight();
            // Case where right side is heavy
            if (getBalance(cur_node) == -2) {
                // If there is a zigzag rotate right child right to get a straight line
                if (getBalance(cur_node.right) == 1) {
                    rotateRight(cur_node.right);
                }
                // Then rotate left to balance
                rotateLeft(cur_node);
            // Case where left side is heavy
            } else if (getBalance(cur_node) == 2) {
                // If there is a zigzag rotate left child left to get a straight line
                if (getBalance(cur_node.left) == -1) {
                    rotateLeft(cur_node.left);
                }
                // Then rotate right to balance
                rotateRight(cur_node);
            }
            // If the node is balanced we can move to its parent since height is already updated
        }

        return null;
    }

    private int getBalance(TreeNode<K, V> node) {
        return node.left.height - node.right.height;
    }

    private void rotateLeft(TreeNode<K, V> node) {
        TreeNode<K, V> rightNode = node.right;
        if (rightNode.left != null) {
            node.right = rightNode.left;
        } else {
            node.right = null;
        }
        rightNode.left = node;
        node.updateHeight();
        rightNode.updateHeight();
    }

    private void rotateRight(TreeNode<K, V> node) {
        TreeNode<K, V> leftNode = node.left;
        if (leftNode.right != null) {
            node.left = leftNode.right;
        } else {
            node.left = null;
        }
        leftNode.right = node;
        node.updateHeight();
        leftNode.updateHeight();
    }
}

