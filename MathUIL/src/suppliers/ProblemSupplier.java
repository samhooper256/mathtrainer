package suppliers;

import java.util.*;
import java.util.function.Supplier;

import problems.Problem;
import utils.Ref;

/**
 * @author Sam Hooper
 *
 */
@FunctionalInterface
public interface ProblemSupplier extends Supplier<Problem> {
	default Collection<Ref> settings() {
		return Collections.emptySet();
	}
}
