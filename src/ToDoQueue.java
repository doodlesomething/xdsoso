/*
 * @author doodlesomething
 * @date:2014-12-30
 * @version:1.0
 * @description UrlObject priorityqueue  构造合适的优先级队列
 */

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Comparator;


public class ToDoQueue {
	private static Queue<UrlObject> todoQueue;
	
	
	/*
	 * @description:overwrite the compare to define ourself PriroityQueue
	 */
	private static Comparator<UrlObject> OrderUrlObject = new Comparator<UrlObject>() {
		public int compare(UrlObject objA,UrlObject objB) {
			return objB.priority - objA.priority;
		}
	};
	
	
	/*
	 * @description:init 
	 */
	public ToDoQueue() {
		todoQueue = new PriorityQueue<UrlObject>(100,OrderUrlObject);	
	}
	

	
	/*
	 * @description:add url object to PriorityQueue
	 */
	public void add(UrlObject UrlObjectObj) {
		todoQueue.add(UrlObjectObj);
	}
	
	/*
	 * @description:get only url
	 */
	public String getUrl() {
		return todoQueue.poll().url;
	}
	
	/*
	 * @description:get url object
	 */
	public UrlObject get() {
		return todoQueue.poll();
	}
	
	public boolean isEmpty() {
		if (todoQueue.size() <= 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	public int size() {
		return todoQueue.size();
	}
		
}



/*
 * 
44754
32768
40890
 */



