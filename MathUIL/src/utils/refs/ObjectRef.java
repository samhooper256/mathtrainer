package utils.refs;

import utils.ChangeListener;

/**
 * 
 * @author Sam Hooper
 */
public interface ObjectRef<T> extends Ref {
	
	static <T> ObjectRef<T> of(T value) {
		
		class ConstantObjectRef<S> extends AbstractObjectRef<S> {
			
			private final S value;
			
			public ConstantObjectRef(final S initialValue) {
				this.value = initialValue;
			}
			
			@Override
			public S getValue() {
				return value;
			}
			
		}
		
		return new ConstantObjectRef<>(value);
	}
	
	T getValue();
	
	void addChangeListener(ChangeListener<T> listener);
	
	boolean removeChangeListener(ChangeListener<T> listener);
	
}
