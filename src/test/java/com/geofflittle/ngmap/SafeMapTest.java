package com.geofflittle.ngmap;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SafeMapTest {

    private SafeMap<Integer, Integer> safeMap;

    @Before
    public void before() {
        safeMap = SafeMap.newSafeMap();
    }

    @Test
    public void containsWrappedAndUnwrapped() {
        safeMap.put(1, Optional.of(2));

        assertTrue(safeMap.containsValue(Optional.of(2)));
        assertTrue(safeMap.containsValue(2));
    }

    @Test
    public void canGetExisting() {
        safeMap.put(1, Optional.of(2));

        assertEquals(Optional.of(2), safeMap.get(1));
    }

    @Test
    public void noPutGetsEmpty() {
        assertEquals(Optional.empty(), safeMap.get(1));
    }

    @Test
    public void putEmptyGetsEmpty() {
        safeMap.put(1, Optional.empty());

        assertEquals(Optional.empty(), safeMap.get(1));
    }

}
