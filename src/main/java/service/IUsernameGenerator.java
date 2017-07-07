package service;

import java.util.List;

/**
 * @author victor.
 */
public interface IUsernameGenerator {
    List<String> generateSuggestions(String initial);
}
