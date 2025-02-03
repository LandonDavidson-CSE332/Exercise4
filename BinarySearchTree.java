import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BinarySearchTree<K extends Comparable<K>, V> implements OrderedDeletelessDictionary<K,V> {

    // Fields were made public to assist with autograding
    // Do not change the visibility of these fields :)
    public TreeNode<K, V> root;
    public int size;

    public BinarySearchTree(){
        root = null;
        size = 0;
    }

    public int size(){
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public V find(K key){
        if(root == null){
            throw new IllegalStateException();
        }
        return find(key, root);
    }

    //recursive helper
    private V find(K key, TreeNode<K,V> curr){
        if(curr == null){
            return null;
        }
        int currMinusNew = curr.key.compareTo(key); // negative if curr is smaller
        if(curr.key.equals(key)){
            return curr.value;
        } else if(currMinusNew < 0){
            // curr is smaller so key must be to the right.
            return find(key, curr.right);
        } else{
            return find(key, curr.left);
        }
    }

    // Returns the smallest key which is greater than the given key
    // Returns null if the given key is greater than or equal to the largest
    // key present
    public K findNextKey(K key){
        // Stack is used to traverse back up the tree and find the smallest key larger than the given one
        Stack<TreeNode<K, V>> stack = new Stack<>();
        TreeNode<K, V> cur_node = root;
        // Set to false if we take a left (found a key larger than the given one)
        boolean isMax = true;
        // Loop until we find the given key
        while (cur_node.key != key) {
            // If current node doesn't have children then key doesn't exist
            if (cur_node.height == 0) {
                return null;
            }
            // Add the current node (our next node's parent) to the stack
            stack.add(cur_node);
            // If key is less than current node's key go left
            if (key.compareTo(cur_node.key) < 0) {
                cur_node = cur_node.left;
                isMax = false;
            // Else if key is greater than or equal to current node's key go right
            } else {
                cur_node = cur_node.right;
            }
        }
        // If we only took right turns than the given key is the max and there isn't a next key
        if (isMax) {
            return null;
        }
        // If the given key has a right child then we go right and repeatedly left
        // then return the key of the first node without a left child
        // (the smallest of the nodes bigger than the given key)
        if (cur_node.right != null) {
            cur_node = cur_node.right;
            while (cur_node.left != null) {
                cur_node = cur_node.left;
            }
            return cur_node.key;
        }
        // If the given key doesn't have a right child then go up until we find a parent with a larger key
        // The parent is guaranteed to be the next key since anything to the right is going to be bigger
        while (!stack.isEmpty()) {
            cur_node = stack.pop();
            if (cur_node.key.compareTo(key) > 0) {
                return cur_node.key;
            }
        }
        return cur_node.key;
    }

    // Returns the largest key which is less than the given key
    // Returns null if the given key is less than or equal to the smallest
    // key present
    public K findPrevKey(K key){
        // TODO
        return null;
    }

    public V insert(K key, V value){
        V answer = find(key, root);
        if(answer == null){
            size++;
        }
        root = insert(key, value, root);
        root.updateHeight();
        return answer; 
    }

    private TreeNode<K,V> insert(K key, V value, TreeNode<K,V> curr){
        if (curr == null){
            return new TreeNode<>(key, value);
        }
        int currMinusNew = curr.key.compareTo(key);
        if(currMinusNew==0){
            curr.value = value;
        } else if(currMinusNew < 0){
            curr.right = insert(key, value, curr.right);
        } else{
            curr.left = insert(key, value, curr.left);
        }
        curr.updateHeight();
        return curr;
    }

    public List<V> getValues(){
        List<V> values = new ArrayList<>();
        inorderFillValues(values, root);
        return values;
    }

    private void inorderFillValues(List<V> values, TreeNode<K,V> curr){
        if(curr != null){
            inorderFillValues(values, curr.left);
            values.add(curr.value);
            inorderFillValues(values, curr.right);
        }
    }

    public List<K> getKeys(){
        List<K> keys = new ArrayList<K>();
        inorderFillKeys(keys, root);
        return keys;
    }

    private void inorderFillKeys(List<K> keys, TreeNode<K,V> curr){
        if(curr != null){
            inorderFillKeys(keys, curr.left);
            keys.add(curr.key);
            inorderFillKeys(keys, curr.right);
        }
    }

    public void printSideways() {
        printSideways(root, 0);
    }

    private void printSideways(TreeNode<K,V> root, int level) {
        if (root != null) {
            printSideways(root.right, level + 1);
            for (int i = 0; i < level; i++) {
                System.out.print("    ");
            }
            System.out.println(root.key);
            printSideways(root.left, level + 1);
        }
    }
}
