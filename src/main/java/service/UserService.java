package service;

import data.IUserProvider;
import data.Result;
import data.User;
import org.springframework.beans.factory.annotation.Autowired;
import validation.IUserValidator;

import java.util.Optional;

/**
 * Service class with main logic to check for username availability.
 * @author victor.
 */
public class UserService implements IUserService {
    @Autowired private IUserValidator userValidator;
    @Autowired private IUserProvider userProvider;
    @Autowired private IUsernameGenerator usernameGenerator;

    // Usage of "Chain of Responsibility" pattern to define a set of checks that will lead to a result of authorizing
    // the provided username or take the respective actions.
    // This way the checking logic is encapsulated in each handler making it more maintainable and easy to change in
    // the future.
    private final IHandler<String, Result> handlerChain =
            new UsernameAvailabilityHandler(
                new UsernameRestrictionHandler(
                        new UsernameAllowedHandler()));

    /**
     * Checks if the provided username is OK after applying the defined handlers.
     * @param wantedUsername provided username
     * @return Result object with information about the actual check.
     */
    public Result checkUsername(final String wantedUsername) {
        final String username = wantedUsername.trim();
        System.out.println(String.format("Checking '%s' as '%s'", wantedUsername, username));

        try {
            userValidator.validateUsername(username);
        } catch (RuntimeException e) {
            return new Result(false, null, e.getMessage());
        }

        final Result result = new Result(false, null, null);
        handlerChain.handle(username, result);

        return result;
    }

    private final class UsernameAvailabilityHandler extends UsernameHandler<String, Result> {
        public UsernameAvailabilityHandler(IHandler<String, Result> next) {
            super(next);
        }

        @Override
        public void handle(final String username, final Result result) {
            Optional<User> userOpt = userProvider.get(username);
            if (userOpt.isPresent()) {
                result.setSuccess(false);
                result.setMessg(
                    String.format(
                        "Provided username '%s' is already taken. Here is some suggestions.",
                        username));
                result.setSuggestedUsernames(usernameGenerator.generateSuggestions(username));
                return;
            }

            handleNext(username, result);
        }
    }

    private final class UsernameRestrictionHandler extends UsernameHandler<String, Result> {
        public UsernameRestrictionHandler(IHandler<String, Result> next) {
            super(next);
        }

        @Override
        public void handle(final String username, final Result result) {
            if (userValidator.isRestricted(username)) {
                result.setSuccess(false);
                result.setMessg(
                    String.format(
                        "Provided username '%s' contains a restricted word. Here is some suggestions.",
                        username));
                result.setSuggestedUsernames(usernameGenerator.generateSuggestions(username));
                return;
            }

            handleNext(username, result);
        }
    }

    private final class UsernameAllowedHandler implements IHandler<String, Result> {
        @Override
        public void handle(final String handled, final Result result) {
            result.setSuccess(true);
            result.setMessg(String.format("Provided username '%s' is available", handled));
        }
    }
}
