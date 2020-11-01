package utils;

/**
 * A reference to some data. Subtypes of {@link Ref} are encouraged but not required to allow for {@link Listener Listeners} to
 * listen to changes to the data they refer to. {@link Ref Refs} may or may not allow for the data they refer to to be changed
 * through the {@link Ref}. {@link Ref Refs} are not required to make the data they refer to publicly available.
 * 
 * @author Sam Hooper
 *
 */
public interface Ref {

}
