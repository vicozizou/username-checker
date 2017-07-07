package service;

import java.util.Random;

/**
 * Base class with utility code for extending decorators.
 * @author victor.
 */
public abstract class BaseTextDecorator implements TextDecorator {
    protected static final Random RANDOM = new Random();

    private TextDecorator wrapped;

    public BaseTextDecorator(TextDecorator wrapped) {
        if (wrapped == null) {
            throw new IllegalArgumentException("Wrapped decorator cannot be null");
        }
        this.wrapped = wrapped;
    }

    public TextDecorator getWrapped() {
        return wrapped;
    }

    /**
     * Tells the current decorator if any decoration should be executed
     * @return true if it is, false if not
     */
    protected final boolean allowDecoration() {
        return RANDOM.nextBoolean();
    }
}
