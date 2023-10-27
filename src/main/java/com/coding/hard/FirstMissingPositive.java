package hard;

public class FirstMissingPositive {

    public static int indexOf(int[] arr, int ele) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == ele) return i;
        }
        return -1;
    }

    public static int firstMissingPositive(int[] nums) {
        if (nums.length == 1) {
            if (nums[0] == 1) return 2;
            return 1;
        }
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (int num : nums) {
            if (num <= 0) continue;
            max = Math.max(max, num);
            min = Math.min(min, num);
        }
        int ans = 0;
        if (min > 1) return 1;
        for (int i = min + 1; i < max; i++) {
            if (indexOf(nums, i) < 0) {
                ans = i;
                break;
            }
        }

        if (ans == 0) {
            ans = max + 1;
        }
        return ans;
    }

    public static int firstMissingPositive1(int[] nums) {
        if (nums == null || nums.length == 0) return 1;

        int i = 0;
        while (i < nums.length) {
            while (nums[i] > 0 && nums[i] <= nums.length && nums[nums[i] - 1] != nums[i]) {
                int digit = nums[i];
                int swapIndex = digit - 1;

                int tmp = nums[swapIndex];
                nums[swapIndex] = digit;
                nums[i] = tmp;
            }
            i++;
        }

        for (int j = 0; j < nums.length; j++) {
            if (nums[j] != j + 1) {
                return j + 1;
            }
        }
        return nums.length + 1;
    }

    public static void main(String[] args) {
        int[] nums = {3,4,-1,1};
        System.out.println(firstMissingPositive(nums));
        System.out.println(firstMissingPositive1(nums));
    }
}
