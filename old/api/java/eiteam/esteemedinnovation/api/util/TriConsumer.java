package eiteam.esteemedinnovation.api.util;

import javax.annotation.Nonnull;

/**
 * Basically a {@link java.util.function.BiConsumer} but takes 3 parameters instead of 2.
 */
@FunctionalInterface
public interface TriConsumer<A, B, C> {
    void accept(A a, B b, C c);

    default TriConsumer<A, B, C> andThen(@Nonnull TriConsumer<? super A, ? super B, ? super C> after) {
        return (a, b, c) -> {
            accept(a, b, c);
            after.accept(a, b, c);
        };
    }
}
