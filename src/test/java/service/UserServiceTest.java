package service;

import data.IUserProvider;
import data.Result;
import data.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import validation.IUserValidator;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * @author victor.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock private IUserValidator userValidator;
    @Mock private IUserProvider userProvider;
    @Mock private IUsernameGenerator usernameGenerator;
    @InjectMocks private UserService userService;
    private Optional<User> emptyUserOpt;
    private Optional<User> userOpt;


    @Before
    public void setup() {
        emptyUserOpt = Optional.empty();
        userOpt = Optional.of(new User("myUsername"));
        when(userProvider.get(anyString())).thenReturn(emptyUserOpt);
        when(userValidator.isRestricted(anyString())).thenReturn(false);
        when(usernameGenerator.generateSuggestions(anyString())).thenReturn(Arrays.asList("myUsername100", "myUsernameMyUsername", "my_Username", "myUsername_2017"));
    }

    @Test
    public void test_checkUsername() {
        // username is available and not restricted
        Result result = userService.checkUsername("myUsername");
        assertThat(result.isSuccess(), is(true));
        assertThat(result.getSuggestedUsernames().isEmpty(), is(true));
        assertThat(result.getMessg(), is("Provided username 'myUsername' is available"));

        // user already taken
        when(userProvider.get(anyString())).thenReturn(userOpt);
        result = userService.checkUsername("myUsername");
        assertThat(result.isSuccess(), is(false));
        assertThat(result.getSuggestedUsernames().isEmpty(), is(false));
        assertThat(result.getMessg(), is("Provided username 'myUsername' is already taken. Here is some suggestions."));

        // username is restricted
        when(userProvider.get(anyString())).thenReturn(emptyUserOpt);
        when(userValidator.isRestricted(anyString())).thenReturn(true);
        result = userService.checkUsername("myUsername");
        assertThat(result.isSuccess(), is(false));
        assertThat(result.getSuggestedUsernames().isEmpty(), is(false));
        assertThat(result.getMessg(), is("Provided username 'myUsername' contains a restricted word. Here is some suggestions."));
    }

    @Test
    public void test_checkUsername_invalidUsername() {
        doThrow(RuntimeException.class).when(userValidator).validateUsername(anyString());
        Result result = userService.checkUsername("myUsername");
        assertThat(result.isSuccess(), is(false));
        assertThat(result.getSuggestedUsernames().isEmpty(), is(true));
    }
}
