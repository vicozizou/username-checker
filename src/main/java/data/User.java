package data;

import java.util.Objects;

/**
 * Represents a User object.
 * Actually not that necessary since al could be done with strings representing the usernames.
 * @author victor.
 */
public class User implements IUser {
    private String username;

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return null;
    }

    //this is going on a set later
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
