package utils.function;

import java.util.*;
import java.util.function.*;

/**
 * 
 */
//Code (essentially) copied from OptionalInt but with 'int' replaced by 'char'.
public class OptionalChar {
    /**
     * Common instance for {@link #empty()}.
     */
    private static final OptionalChar EMPTY = new OptionalChar();

    /**
     * If true then the value is present, otherwise indicates no value is present
     */
    private final boolean isPresent;
    private final char value;

    /**
     * Construct an empty instance.
     *
     * @implNote Generally only one empty instance, {@link OptionalChar#EMPTY},
     * should exist per VM.
     */
    private OptionalChar() {
        this.isPresent = false;
        this.value = 0;
    }

    /**
     * Returns an empty {@code OptionalChar} instance.  No value is present for
     * this {@code OptionalChar}.
     *
     * @apiNote
     * Though it may be tempting to do so, avoid testing if an object is empty
     * by comparing with {@code ==} against instances returned by
     * {@code OptionalChar.empty()}.  There is no guarantee that it is a singleton.
     * Instead, use {@link #isPresent()}.
     *
     * @return an empty {@code OptionalChar}
     */
    public static OptionalChar empty() {
        return EMPTY;
    }

    /**
     * Construct an instance with the described value.
     */
    private OptionalChar(char value) {
        this.isPresent = true;
        this.value = value;
    }

    /**
     * Returns an {@code OptionalChar} describing the given value.
     */
    public static OptionalChar of(char value) {
        return new OptionalChar(value);
    }

    /**
     * If a value is present, returns the value, otherwise throws
     * {@code NoSuchElementException}.
     */
    public char getAsChar() {
        if (!isPresent) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    /**
     * If a value is present, returns {@code true}, otherwise {@code false}.
     */
    public boolean isPresent() {
        return isPresent;
    }

    /**
     * If a value is not present, returns {@code true}, otherwise
     * {@code false}.
     */
    public boolean isEmpty() {
        return !isPresent;
    }

    /**
     * If a value is present, performs the given action with the value,
     * otherwise does nothing.
     */
    public void ifPresent(CharConsumer action) {
        if (isPresent) {
            action.accept(value);
        }
    }

    /**
     * If a value is present, performs the given action with the value,
     * otherwise performs the given empty-based action.
     */
    public void ifPresentOrElse(CharConsumer action, Runnable emptyAction) {
        if (isPresent) {
            action.accept(value);
        } else {
            emptyAction.run();
        }
    }

    /**
     * If a value is present, returns the value, otherwise returns {@code other}.
     */
    public char orElse(char other) {
        return isPresent ? value : other;
    }

    /**
     * If a value is present, returns the value, otherwise returns the result
     * produced by the supplying function.
     * 
     * @throws NullPointerException if no value is present and the supplying
     *         function is {@code null}
     */
    public char orElseGet(CharSupplier supplier) {
        return isPresent ? value : supplier.getAsChar();
    }

    /**
     * If a value is present, returns the value, otherwise throws {@link NoSuchElementException}.
     */
    public char orElseThrow() {
        if (!isPresent) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    /**
     * If a value is present, returns the value, otherwise throws an exception
     * produced by the exception supplying function.
     */
    public<X extends Throwable> char orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (isPresent) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Indicates whether some other object is "equal to" this
     * {@code OptionalChar}.  The other object is considered equal if:
     * <ul>
     * <li>it is also an {@code OptionalChar} and;
     * <li>both instances have no value present or;
     * <li>the present values are "equal to" each other via {@code ==}.
     * </ul>
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof OptionalChar)) {
            return false;
        }

        OptionalChar other = (OptionalChar) obj;
        return (isPresent && other.isPresent)
                ? value == other.value
                : isPresent == other.isPresent;
    }

    /**
     * Returns the hash code of the value, if present, otherwise {@code 0}
     * (zero) if no value is present.
     */
    @Override
    public int hashCode() {
        return isPresent ? Character.hashCode(value) : 0;
    }

    /**
     * Returns a non-empty string representation of this {@code OptionalChar}
     * suitable for debugging. The exact presentation format is unspecified and
     * may vary between implementations and versions.
     */
    @Override
    public String toString() {
        return isPresent
                ? String.format("OptionalChar[%s]", value)
                : "OptionalChar.empty";
    }
}
