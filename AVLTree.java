import java.util.Stack;

public class AVLTree  <K extends Comparable<K>, V> extends BinarySearchTree<K,V> {

    public AVLTree(){
        super();
    }

    public V insert(K key, V value) {
        if (root == null) {
            root = new TreeNode<>(key, value);
            return null;
        }
        Stack<TreeNode<K, V>> stack = new Stack<>();
        // Search through tree to find insert position while adding parents to the stack
        TreeNode<K, V> cur_node = root;
        while(cur_node != null) {
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
                    break;
                }
                cur_node = cur_node.right;
            } else {
                // If there is an empty spot where the new node should go
                // create it with no children so we leave the loop
                if (cur_node.left == null) {
                    cur_node.left = new TreeNode<>(key, value);
                    break;
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
                    rotateRight(cur_node.right, cur_node);
                }
                TreeNode<K, V> parent = !stack.isEmpty() ? stack.peek() : null;
                // Then rotate left to balance
                rotateLeft(cur_node, parent);
            // Case where left side is heavy
            } else if (getBalance(cur_node) == 2) {
                // If there is a zigzag rotate left child left to get a straight line
                if (getBalance(cur_node.left) == -1) {
                    rotateLeft(cur_node.left, cur_node);
                }
                TreeNode<K, V> parent = !stack.isEmpty() ? stack.peek() : null;
                // Then rotate right to balance
                rotateRight(cur_node, parent);
            }
            // If the node is balanced we can move to its parent since height is already updated
        }
        return null;
    }

    private int getBalance(TreeNode<K, V> node) {
        // If a node doesn't exist use -1 for its height
        int leftHeight = node.left != null ? node.left.height : -1;
        int rightHeight = node.right != null ? node.right.height : -1;
        return leftHeight - rightHeight;
    }

    private void rotateLeft(TreeNode<K, V> node, TreeNode<K, V> parent) {
        // Store the right node of node since we are about to overwrite it
        TreeNode<K, V> rightNode = node.right;
        // If node is root set rightNode to root
        if (parent == null) {
            root = rightNode
        // Otherwise set rightNode to the proper child of parent (compare keys)
        } else {
            if (parent.key.compareTo(rightNode.key) > 0) {
                parent.left = rightNode;
            } else {
                parent.right  = rightNode
            }
        }
        // If node.right.left isn't null we need to move it to node.right
        if (rightNode.left != null) {
            node.right = rightNode.left;
        // Otherwise null node.right
        } else {
            node.right = null;
        }
        rightNode.left = node;
        // Update node heights, first node (the new child) then rightNode (the new parent)
        node.updateHeight();
        rightNode.updateHeight();
    }

    private void rotateRight(TreeNode<K, V> node, TreeNode<K, V> parent) {
        // Store the left node of node since we are about to overwrite it
        TreeNode<K, V> leftNode = node.left;
        // If node is root set leftNode to root
        if (parent == null) {
            root = leftNode
        // Otherwise set leftNode to the proper child of parent (compare keys)
        } else {
            if (parent.key.compareTo(leftNode.key) > 0) {
                parent.left = leftNode;
            } else {
                parent.right  = leftNode
            }
        }
        // If node.left.right isn't null we need to move it to node.left
        if (leftNode.right != null) {
            node.left = leftNode.right;
        // Otherwise null node.left
        } else {
            node.left = null;
        }
        leftNode.right = node;
        // Update node heights, first node (the new child) then leftNode (the new parent)
        node.updateHeight();
        leftNode.updateHeight();
    }
}

