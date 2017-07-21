package com.geofflittle.ngmap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class BreadthFirstSearchTest {

    @Test
    public void add() {
        Map<Integer, List<Integer>> adjList = new HashMap<>();
        adjList.put(1, Lists.newArrayList(2, 3));
        adjList.put(2, Lists.newArrayList(1, 4, 5));
        adjList.put(3, Lists.newArrayList(1, 5, 6));
        adjList.put(4, Lists.newArrayList(2, 7, 8));
        adjList.put(5, Lists.newArrayList(2, 3, 8));
        adjList.put(6, Lists.newArrayList(3, 8, 9));
        adjList.put(7, Lists.newArrayList(4, 10, 11));
        adjList.put(8, Lists.newArrayList(4, 5, 6, 11, 12, 13));
        adjList.put(9, Lists.newArrayList(6, 13, 14));
        adjList.put(10, Lists.newArrayList(7, 15));
        adjList.put(11, Lists.newArrayList(7, 8, 15));
        adjList.put(12, Lists.newArrayList(8, 15, 16));
        adjList.put(13, Lists.newArrayList(8, 9, 16));
        adjList.put(14, Lists.newArrayList(9, 16));
        adjList.put(15, Lists.newArrayList(10, 11, 12));
        adjList.put(16, Lists.newArrayList(12, 13, 14));

        BreadthFirstSearch<Integer> bfs = BreadthFirstSearch.breadthFirstSearch(1, i -> adjList.get(i));

        assertEquals(IntStream.range(1, 17).boxed().collect(Collectors.toList()), ImmutableList.builder().addAll(bfs).build());
    }

}
