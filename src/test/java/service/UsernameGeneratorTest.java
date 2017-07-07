package service;

import data.IUserProvider;
import data.User;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import validation.IUserValidator;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author victor.
 */
@RunWith(MockitoJUnitRunner.class)
public class UsernameGeneratorTest {
    @Mock private IUserValidator userValidator;
    @Mock private IUserProvider userProvider;
    @InjectMocks private UsernameGenerator usernameGenerator;
    private Optional<User> emptyUserOpt;
    private Optional<User> userOpt;

    @Before
    public void setup() {
        emptyUserOpt = Optional.empty();
        userOpt = Optional.of(new User("myUsername"));
        when(userProvider.get(anyString())).thenReturn(emptyUserOpt);
        when(userValidator.isRestricted(anyString())).thenReturn(false);
    }

    @Test
    public void test_generateSuggestions() {
        assertThat(usernameGenerator.generateSuggestions("myUsername").size(), Is.is(14));
        when(userProvider.get(anyString())).thenReturn(userOpt);
        assertThat(usernameGenerator.generateSuggestions("wrongUsername").size(), Is.is(0));
        when(userProvider.get(anyString())).thenReturn(emptyUserOpt);
        when(userValidator.isRestricted(anyString())).thenReturn(true);
        assertThat(usernameGenerator.generateSuggestions("wrongUsername").size(), Is.is(0));
    }
}
