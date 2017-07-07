package service;

import java.util.Optional;

/**
 * Base class with utility logic for extending handlers.
 * @author victor.
 */
public abstract class UsernameHandler<E, R> implements IHandler<E, R> {
    private IHandler<E, R> next;

    public UsernameHandler(IHandler<E, R> next) {
        this.next = next;
    }

    protected void handleNext(E handled, R result) {
        Optional<IHandler<E, R>> handlerOpt = Optional.ofNullable(next);
        if (handlerOpt.isPresent()) {
            handlerOpt.get().handle(handled, result);
        }
    }
}
