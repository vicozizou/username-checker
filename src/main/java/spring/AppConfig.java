package spring;

import data.IUserProvider;
import data.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import data.UserProvider;
import service.IUserService;
import service.IUsernameGenerator;
import service.UserService;
import service.UsernameGenerator;
import validation.IUserValidator;
import validation.UserValidator;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Spring configuration class.
 * @author victor.
 */
@Configuration
@PropertySource(value = "classpath:app.properties", ignoreResourceNotFound = true)
public class AppConfig {
    @Value("${usernameMinLength?:6}")
    private int usernameMinLength;

    @Value("${existentUsers}")
    private String existentUsers;

    @Value("${restrictedWords}")
    private String restrictedWords;

    @Bean
    public IUserValidator userValidator() {
        return new UserValidator(
                usernameMinLength,
                Arrays.stream(restrictedWords.split(",")).collect(Collectors.toSet()));
    }

    @Bean
    public IUserProvider userProvider() {
        return new UserProvider(
                Arrays
                    .stream(existentUsers.split(","))
                    .map(User::new)
                    .collect(Collectors.toSet()));
    }

    @Bean
    public IUserService userService() {
        return new UserService();
    }

    @Bean
    public IUsernameGenerator usernameGenerator() {
        return new UsernameGenerator();
    }
}
