package util;

import java.util.HashMap;
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

	public Map<T, Integer> getMap() {
		return counter;
	}
}
