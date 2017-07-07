package service;

/**
 * @author victor.
 */
public interface IHandler<E, R> {
    void handle(E handled, R result);
}
