package dsa.tree;

public class BinaryIndexTree {
    final static int MAX = 1000;
    static int[] BITree = new int[MAX];

    static int getSum(int index) {
        int sum = 0;
        while (index > 0) {
            sum += BITree[index];
            index -= index & (-index);
        }
        return sum;
    }

    static void updateBIT(int n, int index, int val) {
        index = index + 1;
        while (index <= n) {
            BITree[index] += val;
            index += index & (-index);
        }
    }
    static void constructBITree(int[]arr,int n){
        for (int i = 1; i <=n ; i++) {
            BITree[i]=0;
        }
        for (int i = 0; i < n; i++) {
            updateBIT(n,i,arr[i]);
        }
    }

    public static void main(String[] args) {
        int freq[] = {2, 1, 1, 3, 2, 3,
                4, 5, 6, 7, 8, 9};
        int n = freq.length;
        constructBITree(freq,n);
       int s = getSum(5);
        System.out.println(s);
    }
}
