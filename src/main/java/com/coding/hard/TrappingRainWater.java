package hard;

import java.util.ArrayDeque;
import java.util.Deque;

public class TrappingRainWater {
    public static int trap(int[] height) {
        int ans = 0;
        int i = 0, j = height.length - 1;
        while (i < j) {
            if (height[i] < height[j]) {
                int l;
                for (l = i + 1; (i + 1 < j) && l < j; ) {
                    if (height[i] > height[l]) {
                        ans += (height[i] - height[l]);
                        l++;
                    } else {
                        break;
                    }
                }
                i = l;
            } else {
                int r;
                for (r = j - 1; (j - 1 > i) && r > i; ) {
                    if (height[j] > height[r]) {
                        ans += (height[j] - height[r]);
                        r--;
                    } else {
                        break;
                    }
                }
                j = r;
            }
        }
        return ans;
    }

    static int trap1(int[] height) {
        int left = 0, right = height.length - 1;
        int leftMax = 0, rightMax = 0;
        int totalWater = 0;
        while (left < right) {
            leftMax = Math.max(leftMax, height[left]);
            rightMax = Math.max(rightMax, height[right]);
            if (leftMax < rightMax) {
                totalWater += leftMax - height[left];
                left++;
            } else {
                totalWater += rightMax - height[right];
                right--;
            }
        }
        return totalWater;
    }

    static int trapStack(int[] height) {
        Deque<Integer> stack = new ArrayDeque<>();
        int ans = 0;
        for (int i = 0; i < height.length; i++) {
            while (!stack.isEmpty() && height[stack.peek()] < height[i]) {
                int top = height[stack.peek()];
                stack.pop();
                if (stack.isEmpty()) break;
                int width = i - stack.peek() - 1;
                int len = Math.min(height[i], height[stack.peek()]) - top;
                ans += len * width;
            }
            stack.push(i);
        }
        return ans;
    }

    public static void main(String[] args) {
        int[] h = {4, 2, 0, 3, 2, 5};
        System.out.println(trapStack(h));
    }
}
