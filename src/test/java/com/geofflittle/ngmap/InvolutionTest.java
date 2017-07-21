package com.geofflittle.ngmap;

import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InvolutionTest {

    private Involution<Integer> involution;

    @Before
    public void before() {
        involution = Involution.involution();
    }

    @Test
    public void addOnlyKeyContains() {
        involution.put(1);

        assertTrue(involution.containsElement(1));
    }

    @Test
    public void addOnlyKeyGetsEmpty() {
        involution.put(1);

        assertEquals(Optional.empty(), involution.get(1));
    }

    @Test
    public void noAddDoesntContain() {
        assertFalse(involution.containsElement(1));
    }

    @Test
    public void addContainsValue() {
        involution.put(1, 2);

        assertTrue(involution.containsCoelement(2));
    }

    @Test
    public void addCanGet() {
        involution.put(1, 2);

        assertEquals(Optional.of(2), involution.get(1));
    }

    @Test
    public void addsInverse() {
        involution.put(1, 2);

        assertEquals(Optional.of(1), involution.get(2));
    }

    @Test
    public void oneAddTwoInDomain() {
        involution.put(1, 2);

        assertEquals(ImmutableSet.of(1, 2), involution.domainSet());
    }

    @Test(expected = IllegalStateException.class)
    public void cantAddFixedPoint() {
        involution.put(1, 1);
    }

    @Test(expected = IllegalStateException.class)
    public void cantOverwriteExistingKey() {
        involution.put(1, 2);
        involution.put(1, 3);
    }

    @Test(expected = IllegalStateException.class)
    public void cantOverwriteExistingValue() {
        involution.put(1, 2);
        involution.put(2, 3);
    }

    @Test
    public void removesInverseFromKey() {
        involution.put(1, 2);
        involution.remove(1);

        assertFalse(involution.containsElement(1));
        assertFalse(involution.containsElement(2));
    }

    @Test
    public void removesInverseFromValue() {
        involution.put(1, 2);
        involution.remove(2);

        assertFalse(involution.containsElement(1));
        assertFalse(involution.containsElement(2));
    }

}
