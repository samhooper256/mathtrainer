package suppliers;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

@Target(TYPE)
@Retention(RUNTIME)
/**
 * @author Sam Hooper
 *
 */
public @interface Categorized {
	Category category();
}
