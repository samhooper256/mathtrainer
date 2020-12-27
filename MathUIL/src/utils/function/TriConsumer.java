package utils.function;

import java.util.Objects;
import java.util.function.*;

/**
 * <p>Represents an operation that accepts three input arguments and returns no result. This is the three-arity specialization of {@link Consumer}.
 * Unlike most other functional interfaces, {@code TriConsumer} is expected to operate via side-effects.</p>
 * <p>The functional method of this functional interface is {@link #accept(Object, Object, Object)}.</p>
 * 
 * @param <T> the type of the first argument to the operation
 * @param <S> the type of the second argument to the operation
 * @param <U> the type of the third argument to the operation
 * 
 * @see Consumer
 *
 */
@FunctionalInterface
public interface TriConsumer<T, S, U> {
	
	/**
     * Performs this operation on the given arguments.
     */
	void accept(T t, S s, U u);
	
	/**
     * Returns a composed {@link TriConsumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code TriConsumer} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default TriConsumer<T, S, U> andThen(TriConsumer<? super T, ? super S, ? super U> after) {
        Objects.requireNonNull(after);

        return (t, s, u) -> {
            accept(t, s, u);
            after.accept(t, s, u);
        };
    }
    
}
