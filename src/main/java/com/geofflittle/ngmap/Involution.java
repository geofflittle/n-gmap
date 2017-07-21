package com.geofflittle.ngmap;

import com.google.common.base.Preconditions;
import lombok.NonNull;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Models a partial involution without fixed point.
 *   Partial: Allows mappings with an "empty" value.
 *   Involution: Always f(f(x)) = x
 *   Without Fixed Point: Never f(x) = x
 * @param <E> the domain and range of the Involution
 */
public class Involution<E> {

    @NonNull
    private final SafeMap<E, E> safeMap;

    private Involution() {
        this.safeMap = SafeMap.newSafeMap();
    }

    public static <E> Involution<E> involution() {
        return new Involution<E>();
    }

    public boolean containsElement(@NonNull E element) {
        return safeMap.containsKey(element);
    }

    public boolean containsCoelement(@NonNull E coelement) {
        return safeMap.containsValue(coelement);
    }

    /**
     * Retrieves the set of the domain of this Involution.  We assume that there are no values which are not also keys
     * (given it being an Involution) and therefore only return the set of keys.
     * @return the domain set
     */
    public Set<E> domainSet() {
        return safeMap.entrySet().stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    public Optional<E> get(@NonNull E element) {
        return safeMap.get(element);
    }

    /**
     * Convenience method to put an element in the domain of the Involution without a value
     * @param element
     */
    public void put(@NonNull E element) {
        safeMap.put(element, Optional.empty());
    }

    /**
     * Associates two elements, a and b, such that f(a) = b and f(b) = a.
     * Fixed points prohibited. Overwriting non-empty values prohibited.
     * @param element
     * @param coelement
     */
    public void put(@NonNull E element, @NonNull E coelement) {
        Preconditions.checkState(!element.equals(coelement), "Fixed points prohibited");
        checkNoKeyOrEmptyValue(element);
        checkNoKeyOrEmptyValue(coelement);

        safeMap.put(coelement, Optional.of(element));
        safeMap.put(element, Optional.of(coelement));
    }

    private void checkNoKeyOrEmptyValue(E element) {
        Preconditions.checkState(!safeMap.containsKey(element) || !safeMap.get(element).isPresent(),
                "The element is already associated with a value");
    }

    /**
     * Removes both mappings associated with an element a, a -> b and b -> a
     * @param element
     */
    public void remove(@NonNull E element) {
        // Remove inverse
        safeMap.get(element).ifPresent(safeMap::remove);
        safeMap.remove(element);
    }

}
