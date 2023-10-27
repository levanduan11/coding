package hard;

import java.util.ArrayList;
import java.util.List;

public class ReverseNodesKGroup {

    public static int size(ListNode head) {
        int res = 0;
        for (ListNode i = head; i != null; i = i.next) {
            res++;
        }
        return res;
    }

    public static ListNode findTail(ListNode head, int k) {
        ListNode c = head;
        for (int i = 0; i < k; i++) {
            c = c.next;
        }
        return c;
    }

    public static ListNode findTail(ListNode head) {
        ListNode pre = null;
        for (ListNode i = head; i != null; i = i.next) pre = i;
        return pre;
    }

    public static ListNode reverseKGroup(ListNode head, int k) {
        int size = size(head);
        if (k == 1) return head;

        int n = (size / k);
        int remain = size % k;
        List<ListNode> slice = new ArrayList<>();
        ListNode current = head;
        for (int i = 0; i < n; i++) {
            slice.add(current);
            current = findTail(current, k);
        }
        ListNode remainNode = null;
        if (remain > 0) {
            remainNode = findAt(head, n * k);
        }
        ListNode f = new ListNode(-1);
        ListNode tail = f;
        for (ListNode node : slice) {
            ListNode r = reverse(node, k, 1);
            ListNode t = findTail(r);
            tail.next = r;
            tail = t;
        }
        if (remainNode != null) {
            tail.next = remainNode;
        }
        System.out.println(f);
        return f.next;
    }

    private static ListNode findAt(ListNode head, int i) {
        ListNode curr = head;
        for (int j = 0; j < i; j++) {
            curr = curr.next;
        }
        return curr;
    }

    private static ListNode reverse(ListNode leftStart, int k, int count) {
        if (count == k) {
            return leftStart;
        }

        ListNode newHead = reverse(leftStart.next, k, count + 1);
        leftStart.next.next = leftStart;
        leftStart.next = null;

        return newHead;
    }

    public static int length(ListNode head){
        int len =0;
        ListNode t =head;
        while (t!=null){
            len++;
            t=t.next;
        }
        return len;
    }

    public static ListNode reverse1(ListNode head,int k,int len){
        if(len<k)
            return head;

        int c=0;
        ListNode cur=head;
        ListNode prev=null;
        ListNode temp=null;
        while(cur!=null&&c<k){
            temp=cur.next;
            cur.next=prev;
            prev=cur;
            cur=temp;
            c++;
        }
        if(temp!=null){
            head.next=reverse1(temp,k,len-k);
        }
        return prev;
    }

    public static ListNode reverseKGroup1(ListNode head,int k){
        int l = length(head);
        return reverse1(head,k,l);
    }

    public static void main(String[] args) {
        ListNode a = new ListNode(5);
        ListNode b = new ListNode(4, a);
        ListNode c = new ListNode(3, b);
        ListNode d = new ListNode(2, c);
        ListNode e = new ListNode(1, d);
        int k = 3;
        ListNode res = reverseKGroup(e, 2);
        ListNode res2 = reverseKGroup1(e, 2);
        System.out.println(res);

    }
}
