import java.util.Random;

public class Test {
    public static void main(String[] args) {
        final int n = 20;
        AVLTree<Integer, Integer> dict = new AVLTree<>();
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            int val = r.nextInt(100);
            dict.insert(val, i);
            System.out.println("\nIteration " + i + ": Inserting " + val);
            dict.printSideways();
        }
    }
}
