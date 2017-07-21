package com.geofflittle.ngmap;

import com.google.common.collect.Maps;
import lombok.NonNull;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * This SafeMap wraps a normal Map and guarantees
 *   1. No putting or getting of null values
 *   2. Putting or getting Optional values
 *   3. Checking for value membership with an Optional-wrapped or unwrapped value
 * @param <K>
 * @param <V>
 */
public class SafeMap<K, V> extends AbstractMap<K, Optional<V>> implements Map<K, Optional<V>> {

    @NonNull
    private final Map<K, Optional<V>> unsafeMap;

    private SafeMap() {
        this.unsafeMap = Maps.newHashMap();
    }

    public static <K, V> SafeMap<K, V> newSafeMap() {
        return new SafeMap<K, V>();
    }

    /*
     * Allow value membership check using wrapped or unwrapped value
     */
    @Override
    public boolean containsValue(@NonNull Object value) {
        if (value instanceof Optional) {
            return unsafeMap.containsValue(value);
        }
        return unsafeMap.containsValue(Optional.of(value));
    }

    @Override
    public Set<Entry<K, Optional<V>>> entrySet() {
        return unsafeMap.entrySet();
    }

    /*
     * We must protect in the case that unsafeMap.get() returns null.
     */
    @Override
    public Optional<V> get(@NonNull Object key) {
        return Optional.ofNullable(unsafeMap.get(key)).flatMap(Function.identity());
    }

    @Override
    public Optional<V> put(@NonNull K key, @NonNull Optional<V> value) {
        return unsafeMap.put(key, value);
    }

    @Override
    public Optional<V> remove(@NonNull Object key) {
        return Optional.ofNullable(unsafeMap.remove(key)).flatMap(Function.identity());
    }

}
