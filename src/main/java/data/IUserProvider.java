package data;

import java.util.Optional;

/**
 * @author victor.
 */
public interface IUserProvider {
    Optional<User> get(final String identifier);
}
