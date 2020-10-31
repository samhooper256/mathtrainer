package base;

import utils.FixedDoubleQueue;

/**
 * @author Sam Hooper
 *
 */
public class TimeQueueTester {
	public static void main(String[] args) {
		try {
			FixedDoubleQueue queue = new FixedDoubleQueue(0);
		} catch(IllegalArgumentException e) {}
		FixedDoubleQueue queue = new FixedDoubleQueue(1);
		System.out.println(queue);
		queue.addFirst(1);
		System.out.println(queue);
		queue.addFirst(2);
		System.out.println(queue);
		
		
		FixedDoubleQueue q2 = new FixedDoubleQueue(5);
		
		System.out.println();
		for(int i = 0; i < 20; i++) {
			q2.addFirst(i);
			System.out.println(q2);
		}
	}
}
