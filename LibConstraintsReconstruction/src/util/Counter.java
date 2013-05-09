package util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Counter<T> {

	private Map<T, Integer> counter = new HashMap<T, Integer>();
	
	public void plus1(T object) {
		if(!counter.containsKey(object))
			counter.put(object, 0);
		counter.put(object, counter.get(object) + 1);
	}
	
	public int getCount(T object) {
		if(!counter.containsKey(object))
			return 0;
		return counter.get(object);
	}
	
	public int getTotalCount(){
		int sum = 0;
		Collection<Integer> values = counter.values();
		Iterator<Integer> it = values.iterator();
		while (it.hasNext()){
			sum += it.next();
		}
		return sum;
	}

	public Map<T, Integer> getMap() {
		return counter;
	}
}
