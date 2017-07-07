package validation;

/**
 * @author victor.
 */
public interface IUserValidator {
    void validateUsername(String username);
    boolean isRestricted(String username);
}
