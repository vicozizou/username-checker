package validation;

import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Set;

/**
 * Contains user related validation logic.
 * @author victor.
 */
public class UserValidator implements IUserValidator {
    private final int usernameMinLength;
    private final Set<String> restrictedWords;

    public UserValidator(int usernameMinLength, Set<String> restrictedWords) {
        this.usernameMinLength = usernameMinLength;
        this.restrictedWords = restrictedWords != null ? restrictedWords : Collections.emptySet();
    }

    /**
     * Validates username nullability and length
     * @param username provided username
     */
    public void validateUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new IllegalArgumentException("username must not be empty");
        }

        if (username.length() < usernameMinLength) {
            throw new IllegalArgumentException(String.format("username must be %s characters long", usernameMinLength));
        }
    }

    /**
     * Evaluates if username contains a restricted word.
     * @param username provided username.
     * @return true is username contains any restricted words, otherwise false.
     */
    @Override
    public boolean isRestricted(String username) {
        return restrictedWords.stream().anyMatch(s -> username.contains(s));
    }
}
