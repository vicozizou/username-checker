package data;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * Simulates a User source based on the property found (at classpath) at the "existentUsers" property at app.properties
 * file.
 * @author victor.
 */
public class UserProvider implements IUserProvider {
    private final Set<User> userDb;

    /**
     * Constructor to be loaded by Spring context later.
     * @param userDb a set of User objects parsed from what is foind
     */
    public UserProvider(Set<User> userDb) {
        this.userDb = userDb != null ? userDb : Collections.emptySet();
    }

    /**
     * Gets a Optional object wrapping the actual User which matches the identifier
     * @param username username
     * @return Optional instance wrapping User object when found, otherwise empty.
     */
    @Override
    public Optional<User> get(final String username) {
        User user = new User(username);
        return userDb.contains(user) ? Optional.of(user) : Optional.empty();
    }
}
