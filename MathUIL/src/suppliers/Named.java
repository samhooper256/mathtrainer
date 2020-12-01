package suppliers;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.*;

@Target(TYPE)
@Retention(RUNTIME)
/**
 * @author Sam Hooper
 *
 */
public @interface Named {
	String value();
}
