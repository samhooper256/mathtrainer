package utils.refs;

import java.util.*;

import utils.*;

/**
 * 
 * @author Sam Hooper
 */
public class MutableObjectRef<T> extends AbstractObjectRef<T> {
	
	private T value;
	
	public MutableObjectRef() {
		this(null);
	}
	
	public MutableObjectRef(final T initialValue) {
		this.value = initialValue;
	}
	
	public boolean set(T newValue) {
		if(Objects.equals(newValue, value))
			return false;
		T oldValue = this.value;
		this.value = newValue;
		runChangeListeners(oldValue, newValue);
		runChangeActions();
		return true;
	}
	
	@Override
	public String toString() {
		return String.format("ObjectRef[%s]", value);
	}

	@Override
	public T getValue() {
		return value;
	}
	
}
