package utils;

import java.util.StringJoiner;
import java.util.function.Supplier;

/**
 * A joiner of {@link CharSequence CharSequences} Behaves very similarly to {@link StringJoiner}, but allows for the delimiting {@code CharSequences} to be supplied
 * by a {@link Supplier}.
 * @author Sam Hooper
 *
 */
public class Joiner {
	
	private final StringBuilder builder;
	private final Supplier<? extends CharSequence> delimiterSupplier;
	private int terms;
	
	public Joiner(CharSequence delimiter) {
		this(() -> delimiter);
	}
	
	public Joiner(Supplier<? extends CharSequence> delimiterSupplier) {
		this.builder = new StringBuilder();
		this.delimiterSupplier = delimiterSupplier;
		this.terms = 0;
	}
	
	/**
	 * Adds the given {@link CharSequence} to this {@link Joiner}. Returns {@code this}.
	 */
	public Joiner add(final CharSequence s) {
		if(terms > 0)
			builder.append(delimiterSupplier.get());
		builder.append(s);
		terms++;
		return this;
	}
	
	/**
	 * The number of terms ({@link CharSequence CharSequences}) that have been {@link #add(CharSequence) added} to this {@link Joiner}.
	 */
	public int terms() {
		return terms;
	}
	
}
