package utils;

import java.util.*;
import java.util.function.*;

import utils.function.BooleanConsumer;

/**
 * @author Sam Hooper
 *
 */
public interface BooleanIterator extends PrimitiveIterator<Boolean, BooleanConsumer> {
	
	boolean nextBoolean();
	
	@Override
	default void forEachRemaining(BooleanConsumer action) {
        Objects.requireNonNull(action);
        while (hasNext())
            action.acceptBoolean(nextBoolean());
    }
	
	@Override
	default Boolean next() {
		return nextBoolean();
	}
	
	@Override
    default void forEachRemaining(Consumer<? super Boolean> action) {
        if (action instanceof BooleanConsumer)
            forEachRemaining((BooleanConsumer) action);
        else {
            Objects.requireNonNull(action);
            forEachRemaining((BooleanConsumer) action::accept);
        }
    }
	
}