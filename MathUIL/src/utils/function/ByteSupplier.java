package utils.function;

import java.util.function.Supplier;

/**
 * <p>A {@link Supplier} specialized for the primitive type {@code byte}. The functional method is {@link #getAsByte()}.</p>
 * @author Sam Hooper
 *
 */
public interface ByteSupplier extends Supplier<Byte> {
	
	byte getAsByte();
	
	@Override
	default Byte get() {
		return getAsByte();
	}
	
}
