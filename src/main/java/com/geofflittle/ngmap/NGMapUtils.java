/* Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.geofflittle.ngmap;

public class NGMapUtils {

    /**
     * Adds an "isolated" edge to the NGMap
     *
     * @return
     */
    public static <Attribute> Edge addEdge(NGMap<Attribute> ngMap) {
        NGMap.Dart leftDart = ngMap.addIsolatedDart();
        NGMap.Dart rightDart = ngMap.addIsolatedDart();

        ngMap.sew(leftDart, rightDart, 0);

        return Edge.builder()
                ._1(leftDart)
                ._2(rightDart)
                .build();
    }

    public static <Attribute> NGMap.Dart addConvexPolygon(NGMap<Attribute> ngMap, int edges) {
        Edge source = addEdge(ngMap);
        Edge curr = source;
        for (int i = 0; i < edges - 1; i++) {
            Edge next = addEdge(ngMap);
            ngMap.sew(curr._2, next._1, 1);
            curr = next;
        }
        ngMap.sew(curr._2, source._1, 1);
        return source._1;
    }

}
