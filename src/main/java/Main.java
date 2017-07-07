import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.IUserService;
import service.UserService;
import spring.AppConfig;

import java.util.Arrays;

/**
 * Main entry point.
 * @author victor.
 */
public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No username provided");
        }

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(AppConfig.class);
        ctx.refresh();

        IUserService userService = ctx.getBean(UserService.class);
        Arrays.stream(args).forEach(s -> System.out.println(userService.checkUsername(s)));
    }
}
