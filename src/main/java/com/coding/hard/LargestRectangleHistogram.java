package hard;

import java.util.Arrays;
import java.util.Stack;

public class LargestRectangleHistogram {
    public static int largestRectangleArea(int[] heights) {
        Stack<Integer> stack = new Stack<>();
        int[] row = new int[heights.length + 1];
        row = Arrays.copyOf(heights, heights.length + 1);
        int max = 0;
        int i = 0;
        while (i < row.length) {
            if (stack.isEmpty() || row[stack.peek()] < row[i]) {
                stack.push(i);
                i++;
            } else {
                int currentLowestBarIndex = stack.pop();
                int edge1 = row[currentLowestBarIndex];
                int edge2 = stack.isEmpty() ? i : (i - 1 - stack.peek());

                max = Math.max(max, edge1 * edge2);
            }
        }
        return max;
    }

    static int solution2(int[] heights) {
        final int len = heights.length;
        int stackSize = Math.min(len + 1, 50);
        int[] idxStack = new int[stackSize];
        int[] heightStack = new int[stackSize];
        int stackIndex = 0;
        heightStack[0] = -1;
        int maxArea = 0;

        int area;
        int ht;
        for (int i = len - 1; i >= 0; i--) {
            ht = heights[i];
            while (ht < heightStack[stackIndex]) {
                area = heightStack[stackIndex] * (idxStack[--stackIndex] - i - 1);
                if (area > maxArea) maxArea = area;
            }
            if (ht == heightStack[stackIndex])
                idxStack[stackIndex] = i;
            else {
                idxStack[++stackIndex] = i;
                heightStack[stackIndex] = ht;
            }
        }
        while (stackIndex > 0) {
            area = heightStack[stackIndex] * idxStack[--stackIndex];
            if (area > maxArea) maxArea = area;
        }
        return maxArea;
    }

    public static void main(String[] args) {
        int[] heights = {2, 1, 5, 6, 2, 3};
        System.out.println(largestRectangleArea(heights));
        System.out.println(solution2(heights));
    }
}
