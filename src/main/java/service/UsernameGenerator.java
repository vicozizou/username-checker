package service;

import data.IUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import validation.IUserValidator;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Generates username suggestions strings.
 * @author victor.
 */
public class UsernameGenerator implements IUsernameGenerator {
    private static final int USERNAME_LIST_MAX_SIZE = 14;
    private static final int GENERATE_MAX_ATTEMPTS = 3;
    private static final TextDecorator NOOP = new NoopDecorator();

    @Autowired private IUserValidator userValidator;
    @Autowired private IUserProvider userProvider;

    // Usage of a "Decorator" pattern here to modify the initial string makes the decorating logic to be encapsulated in
    // decorator classes easy to maintain and change later or even adding more decorators (like the commented out
    // VowelsDecorator).
    private final TextDecorator usernameDecorator =
            new DelimiterDecorator(
                    new RandomNumberSuffixDecorator(
                            new RepeatTextDecorator(NOOP)));

    /**
     * Generate a list of suggested username strings based on the initial.
     * It will try to generate at most 14 suggestions unless it has a defined number of failed attempts.
     * An attempt is considered failed when the decorated string is whether already defined, contains a restricted word
     * or it has been generated already.
     * @param initial initial username
     * @return a list of strings representing the suggested username lists
     */
    @Override
    public List<String> generateSuggestions(String initial) {
        final Set<String> usernameSet = new HashSet<>();
        int failAttempts = 0;

        while(usernameSet.size() < USERNAME_LIST_MAX_SIZE && failAttempts < GENERATE_MAX_ATTEMPTS) {
            String decorated = usernameDecorator.decorate(initial);
            if (hasAttemptFailed(decorated)) {
                failAttempts++;
                System.out.println(String.format("Failed attempt #%d for %s", failAttempts, decorated));
                continue;
            }

            if (decorated.equals(initial)) {
                continue;
            }

            usernameSet.add(decorated);
        }

        return usernameSet.stream().sorted().collect(Collectors.toList());
    }

    private boolean hasAttemptFailed(final String decorated) {
        return userProvider.get(decorated).isPresent() || userValidator.isRestricted(decorated);
    }

    // Defined decorators which depending on the situation will modify the initial string not before calling the
    // wrapping decorator instance's decorate() method.
    private static final class NoopDecorator implements TextDecorator {
        @Override
        public String decorate(final String initial) {
            return initial;
        }
    }

    /**
     * Inserts a '_' delimiter in a random position of the initial string
     */
    protected static final class DelimiterDecorator extends BaseTextDecorator {
        private static final char DELIMITER = '_';

        public DelimiterDecorator(TextDecorator wrapped) {
            super(wrapped);
        }

        @Override
        public String decorate(String initial) {
            String decorated = getWrapped().decorate(initial);
            if (!allowDecoration()) {
                return decorated;
            }

            int pos = RANDOM.nextInt(decorated.length() - 1) + 1;
            return decorated.substring(0, pos) + DELIMITER + decorated.substring(pos);
        }
    }

    /*
    // Replaces vowels and "s" letter for a predefined string.
    private static final class VowelsDecorator extends BaseTextDecorator {
        private static final Map<String, String> VOWELS_MAP = createVowelsMap();

        private static Map<String, String> createVowelsMap() {
            Map<String, String> map = new HashMap<>(5);
            map.put("a", "@");
            map.put("e", "3");
            map.put("i", "1");
            map.put("o", "0");
            map.put("u", "#");
            map.put("s", "$");
            return map;
        }

        public VowelsDecorator(TextDecorator wrapped) {
            super(wrapped);
        }

        @Override
        public String decorate(String initial) {
            String decorated = getWrapped().decorate(initial);
            if (!allowDecoration()) {
                return decorated;
            }

            for(String key : VOWELS_MAP.keySet()) {
                String value = VOWELS_MAP.get(key);
                boolean replaceAll = RANDOM.nextBoolean();
                decorated = replaceAll ? decorated.replaceAll(key, value) : decorated.replace(key, value);
            }
            return decorated;
        }
    }*/

    /**
     * Adds a random number between 1 and 10 precessed (or not) by a "_" character acting as a separator.
     */
    protected static final class RandomNumberSuffixDecorator extends BaseTextDecorator {
        private static final int RANGE = 10;

        public RandomNumberSuffixDecorator(TextDecorator wrapped) {
            super(wrapped);
        }

        @Override
        public String decorate(String initial) {
            String decorated = getWrapped().decorate(initial);
            if (!allowDecoration()) {
                return decorated;
            }

            boolean includeSeparator = RANDOM.nextBoolean();
            decorated += (includeSeparator ? "_" : "") + (RANDOM.nextInt(RANGE) + 1);
            return decorated;
        }
    }

    /**
     * Repeats the capitalized version (if it's not already) of the initial string for at most a defined number of times.
     */
    protected static final class RepeatTextDecorator extends BaseTextDecorator {
        private static final int MAX_REPS = 2;

        public RepeatTextDecorator(TextDecorator wrapped) {
            super(wrapped);
        }

        @Override
        public String decorate(String initial) {
            String decorated = getWrapped().decorate(initial);
            if (!allowDecoration()) {
                return decorated;
            }

            final String repeatStr = StringUtils.capitalize(decorated);
            final StringBuilder sb = new StringBuilder(decorated);
            IntStream.range(1, RANDOM.nextInt(MAX_REPS) + 1).forEach(value -> sb.append(repeatStr));

            return sb.toString();
        }
    }
}
