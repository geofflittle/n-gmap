package com.geofflittle.ngmap;

import com.geoffslittle.datastructure.maps.Attribute;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import lombok.Data;
import lombok.Value;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NGMapTest {

    private static final String VERTEX_1 = "vertex-1";
    private static final String VERTEX_2 = "vertex-2";
    private static final String VERTEX_3 = "vertex-3";
    private static final String VERTEX_4 = "vertex-4";
    private static final String EDGE_1 = "edge-1";
    private static final String EDGE_2 = "edge-2";
    private static final String EDGE_3 = "edge-3";
    private static final String EDGE_4 = "edge-4";

    @Test
    public void noArgsConstructorIs0D() {
        NGMap<Object> ngMap = new NGMap<>(0);

        assertEquals(0, ngMap.dimension());
    }

    @Test(expected = IllegalStateException.class)
    public void zeroGMapCantDecreaseDimension() {
        NGMap<Object> ngMap = new NGMap<>(0);

        ngMap.decreaseDimension();
    }

    @Test
    public void increaseDimensionToNPlusOne() {
        NGMap<Object> ngMap = new NGMap<>(0);

        ngMap.increaseDimension();

        assertEquals(1, ngMap.dimension());
    }

    @Test
    public void decreaseDimensionToNMinusOne() {
        NGMap<Object> ngMap = new NGMap<>(0);
        ngMap.increaseDimension();

        ngMap.decreaseDimension();
        assertEquals(0, ngMap.dimension());
    }

    @Test
    public void constructNGMapGivenN() {
        NGMap<Object> ngMap = new NGMap<>(5);

        assertEquals(5, ngMap.dimension());
    }

    @Test
    public void lessThan0NCreates0GMap() {
        NGMap<Object> ngMap = new NGMap<>(-1);

        assertEquals(0, ngMap.dimension());
    }

    @Test
    public void canAddIsolatedDart() {
        NGMap<Object> ngMap = new NGMap<>(0);

        NGMap.Dart isolatedDart = ngMap.addIsolatedDart();

        assertTrue(ngMap.containsDart(isolatedDart));
        assertTrue(ngMap.isIsolated(isolatedDart));
    }

    @Test
    public void canRemoveIsolatedDart() {
        NGMap<Object> ngMap = new NGMap<>(0);

        NGMap.Dart isolatedDart = ngMap.addIsolatedDart();
        ngMap.removeIsolatedDart(isolatedDart);

        assertFalse(ngMap.containsDart(isolatedDart));
        assertTrue(ngMap.isIsolated(isolatedDart));
    }

    @Test
    public void removingNonExistentDartAllowed() {
        NGMap<Object> ngMap = new NGMap<>(0);
        NGMap.Dart nonExistentDart = ngMap.addIsolatedDart();

        ngMap.removeIsolatedDart(nonExistentDart);
        ngMap.removeIsolatedDart(nonExistentDart);

        assertFalse(ngMap.containsDart(nonExistentDart));
        assertTrue(ngMap.isIsolated(nonExistentDart));
    }

    @Test
    public void twoIsolatedDartsAreSewable() {
        NGMap<Object> ngMap = new NGMap<>(0);
        NGMap.Dart leftDart = ngMap.addIsolatedDart();
        NGMap.Dart rightDart = ngMap.addIsolatedDart();

        assertTrue(ngMap.isSewable(leftDart, rightDart, 0));
    }

    @Test(expected = IllegalStateException.class)
    public void cantRemoveNotIsolatedDart() {
        NGMap<Object> ngMap = new NGMap<>(0);
        NGMap.Dart leftDart = ngMap.addIsolatedDart();
        NGMap.Dart rightDart = ngMap.addIsolatedDart();
        ngMap.sew(leftDart, rightDart, 0);

        ngMap.removeIsolatedDart(leftDart);
    }

    @Test
    public void sewTwoDartsNonIsolatedAndReachable() {
        NGMap<Object> ngMap = new NGMap<>(0);
        NGMap.Dart leftDart = ngMap.addIsolatedDart();
        NGMap.Dart rightDart = ngMap.addIsolatedDart();

        ngMap.sew(leftDart, rightDart, 0);

        assertFalse(ngMap.isIsolated(leftDart));
        assertFalse(ngMap.isIsolated(rightDart));

        Iterator<NGMap.Dart> it = ngMap.genericIterator(leftDart, new ArrayList<>(0));
        assertTrue(it.hasNext());
        assertEquals(leftDart, it.next());
        assertTrue(it.hasNext());
        assertEquals(rightDart, it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void unsewTwoDarts() {
        NGMap<Object> ngMap = new NGMap<>(0);
        NGMap.Dart leftDart = ngMap.addIsolatedDart();
        NGMap.Dart rightDart = ngMap.addIsolatedDart();

        ngMap.sew(leftDart, rightDart, 0);
        ngMap.unsew(leftDart, 0);

        assertTrue(ngMap.isIsolated(leftDart));
        assertTrue(ngMap.isIsolated(rightDart));

        Iterator<NGMap.Dart> leftIt = ngMap.genericIterator(leftDart, new ArrayList<>(0));
        assertTrue(leftIt.hasNext());
        assertEquals(leftDart, leftIt.next());
        assertFalse(leftIt.hasNext());

        Iterator<NGMap.Dart> rightIt = ngMap.genericIterator(rightDart, new ArrayList<>(0));
        assertTrue(rightIt.hasNext());
        assertEquals(rightDart, rightIt.next());
        assertFalse(rightIt.hasNext());
    }

    @Test
    public void canGetIsolatedAndSewnDarts() {
        NGMap<Object> ngMap = new NGMap<>(0);
        NGMap.Dart leftDart = ngMap.addIsolatedDart();
        NGMap.Dart rightDart = ngMap.addIsolatedDart();
        NGMap.Dart isolatedDart = ngMap.addIsolatedDart();

        ngMap.sew(leftDart, rightDart, 0);

        assertEquals(Sets.newHashSet(leftDart, rightDart, isolatedDart), ngMap.darts());
    }

    /*
     * TODO: Instead of testing explicit dart configurations, test that, for example, there exists an isomorphism
     * between something that we know is a triangle and what we expect to be a triangle
     */

    /*
     *
     * *-- --*  *-- --*
     * d1   d2  d3   d4
     *    e1       e2
     *
     */
    @Test
    public void create1DManifoldWith3VerticesAnd2Edges() {
        NGMap<Object> ngMap = new NGMap<>(1);
        NGMap.Dart dart1 = ngMap.addIsolatedDart();
        NGMap.Dart dart2 = ngMap.addIsolatedDart();
        NGMap.Dart dart3 = ngMap.addIsolatedDart();
        NGMap.Dart dart4 = ngMap.addIsolatedDart();

        ngMap.sew(dart1, dart2, 0);
        ngMap.sew(dart3, dart4, 0);
        ngMap.sew(dart2, dart3, 1);

        Set<NGMap.Dart> vertex1 = Sets.newHashSet(ngMap.iCell(dart1, 0));
        assertEquals(Sets.newHashSet(dart1), vertex1);

        Set<NGMap.Dart> vertex2 = Sets.newHashSet(ngMap.iCell(dart2, 0));
        assertEquals(Sets.newHashSet(dart2, dart3), vertex2);

        Set<NGMap.Dart> vertex3 = Sets.newHashSet(ngMap.iCell(dart4, 0));
        assertEquals(Sets.newHashSet(dart4), vertex3);

        Set<NGMap.Dart> edge1 = Sets.newHashSet(ngMap.iCell(dart1, 1));
        assertEquals(Sets.newHashSet(dart1, dart2), edge1);

        Set<NGMap.Dart> edge2 = Sets.newHashSet(ngMap.iCell(dart3, 1));
        assertEquals(Sets.newHashSet(dart3, dart4), edge2);
    }

    @Test
    public void create2DManifoldWith4Vertices5EdgesAnd2Faces() {
        NGMap<Object> ngMap = new NGMap<>(2);

        NGMap.Dart triangleSource1 = NGMapUtils.addConvexPolygon(ngMap, 3);
        NGMap.Dart triangleSource2 = NGMapUtils.addConvexPolygon(ngMap, 3);

        ngMap.sew(triangleSource1, triangleSource2, 2);

        // Validate vertices 1-4
//        Set<NGMap.Dart> vertex1 = Sets.newHashSet(ngMap.iCell(face1._1._1, 0));
//        assertEquals(Sets.newHashSet(face1._1._1, face1._3._2, face2._1._1, face2._3._2), vertex1);

//        Set<NGMap.Dart> vertex2 = Sets.newHashSet(ngMap.iCell(dart2, 0));
//        assertEquals(Sets.newHashSet(dart2, dart3, dart8, dart9), vertex2);
//
//        Set<NGMap.Dart> vertex3 = Sets.newHashSet(ngMap.iCell(dart4, 0));
//        assertEquals(Sets.newHashSet(dart4, dart5), vertex3);
//
//        Set<NGMap.Dart> vertex4 = Sets.newHashSet(ngMap.iCell(dart10, 0));
//        assertEquals(Sets.newHashSet(dart10, dart11), vertex4);
//
//        // Validate edges 1-5
//        Set<NGMap.Dart> edge1 = Sets.newHashSet(ngMap.iCell(dart1, 1));
//        assertEquals(Sets.newHashSet(dart1, dart2, dart7, dart8), edge1);
//
//        Set<NGMap.Dart> edge2 = Sets.newHashSet(ngMap.iCell(dart3, 1));
//        assertEquals(Sets.newHashSet(dart3, dart4), edge2);
//
//        Set<NGMap.Dart> edge3 = Sets.newHashSet(ngMap.iCell(dart5, 1));
//        assertEquals(Sets.newHashSet(dart5, dart6), edge3);
//
//        Set<NGMap.Dart> edge4 = Sets.newHashSet(ngMap.iCell(dart9, 1));
//        assertEquals(Sets.newHashSet(dart9, dart10), edge4);
//
//        Set<NGMap.Dart> edge5 = Sets.newHashSet(ngMap.iCell(dart11, 1));
//        assertEquals(Sets.newHashSet(dart11, dart12), edge5);
//
//        // Validate faces 1-2
//        Set<NGMap.Dart> face1 = Sets.newHashSet(ngMap.iCell(dart1, 2));
//        assertEquals(Sets.newHashSet(dart1, dart2, dart3, dart4, dart5, dart6), face1);
//
//        Set<NGMap.Dart> face2 = Sets.newHashSet(ngMap.iCell(dart7, 2));
//        assertEquals(Sets.newHashSet(dart7, dart8, dart9, dart10, dart11, dart12), face2);
    }

    @Test
    public void cubeHas8Vertices12Edges6Faces() {
        NGMap<Object> ngMap = new NGMap<>(3);
        Cube cube = addCube(ngMap);

        // 8 vertices
        Set<NGMap.Dart> vertex1 = Sets.newHashSet(ngMap.iCell(cube._1._1._1, 0));
        assertEquals(Sets.newHashSet(cube._1._1._1, cube._1._4._2, cube._2._3._2, cube._2._4._1, cube._5._1._2,
                cube._5._2._1), vertex1);

        Set<NGMap.Dart> vertex2 = Sets.newHashSet(ngMap.iCell(cube._1._1._2, 0));
        assertEquals(Sets.newHashSet(cube._1._1._2, cube._1._2._1, cube._2._2._2, cube._2._3._1, cube._3._1._1,
                cube._3._4._2), vertex2);

        Set<NGMap.Dart> vertex3 = Sets.newHashSet(ngMap.iCell(cube._1._2._2, 0));
        assertEquals(Sets.newHashSet(cube._1._2._2, cube._1._3._1, cube._3._4._1, cube._3._3._2, cube._4._1._2,
                cube._4._2._1), vertex3);

        Set<NGMap.Dart> vertex4 = Sets.newHashSet(ngMap.iCell(cube._1._3._2, 0));
        assertEquals(Sets.newHashSet(cube._1._3._2, cube._1._4._1, cube._4._1._1, cube._4._4._2, cube._5._2._2,
                cube._5._3._1), vertex4);

        Set<NGMap.Dart> vertex5 = Sets.newHashSet(ngMap.iCell(cube._2._1._1, 0));
        assertEquals(Sets.newHashSet(cube._2._1._1, cube._2._4._2, cube._5._1._1, cube._5._4._2, cube._6._3._2,
                cube._6._4._1), vertex5);

        Set<NGMap.Dart> vertex6 = Sets.newHashSet(ngMap.iCell(cube._2._1._2, 0));
        assertEquals(Sets.newHashSet(cube._2._1._2, cube._2._2._1, cube._3._1._2, cube._3._2._1, cube._6._2._2,
                cube._6._3._1), vertex6);

        Set<NGMap.Dart> vertex7 = Sets.newHashSet(ngMap.iCell(cube._4._2._2, 0));
        assertEquals(Sets.newHashSet(cube._4._2._2, cube._4._3._1, cube._3._2._2, cube._3._3._1, cube._6._1._2,
                cube._6._2._1), vertex7);

        Set<NGMap.Dart> vertex8 = Sets.newHashSet(ngMap.iCell(cube._4._3._2, 0));
        assertEquals(Sets.newHashSet(cube._4._3._2, cube._4._4._1, cube._5._3._2, cube._5._4._1, cube._6._1._1,
                cube._6._4._2), vertex8);

        // 12 edges
        Set<NGMap.Dart> edge1 = Sets.newHashSet(ngMap.iCell(cube._1._1._1, 1));
        assertEquals(Sets.newHashSet(cube._1._1._1, cube._1._1._2, cube._2._3._1, cube._2._3._2), edge1);

        Set<NGMap.Dart> edge2 = Sets.newHashSet(ngMap.iCell(cube._1._2._1, 1));
        assertEquals(Sets.newHashSet(cube._1._2._1, cube._1._2._2, cube._3._4._1, cube._3._4._2), edge2);

        Set<NGMap.Dart> edge3 = Sets.newHashSet(ngMap.iCell(cube._1._3._1, 1));
        assertEquals(Sets.newHashSet(cube._1._3._1, cube._1._3._2, cube._4._1._1, cube._4._1._2), edge3);

        Set<NGMap.Dart> edge4 = Sets.newHashSet(ngMap.iCell(cube._1._4._1, 1));
        assertEquals(Sets.newHashSet(cube._1._4._1, cube._1._4._2, cube._5._2._1, cube._5._2._2), edge4);

        Set<NGMap.Dart> edge5 = Sets.newHashSet(ngMap.iCell(cube._2._4._1, 1));
        assertEquals(Sets.newHashSet(cube._2._4._1, cube._2._4._2, cube._5._1._1, cube._5._1._2), edge5);

        Set<NGMap.Dart> edge6 = Sets.newHashSet(ngMap.iCell(cube._2._2._1, 1));
        assertEquals(Sets.newHashSet(cube._2._2._1, cube._2._2._2, cube._3._1._1, cube._3._1._2), edge6);

        Set<NGMap.Dart> edge7 = Sets.newHashSet(ngMap.iCell(cube._3._3._1, 1));
        assertEquals(Sets.newHashSet(cube._3._3._1, cube._3._3._2, cube._4._2._1, cube._4._2._2), edge7);

        Set<NGMap.Dart> edge8 = Sets.newHashSet(ngMap.iCell(cube._4._4._1, 1));
        assertEquals(Sets.newHashSet(cube._4._4._1, cube._4._4._2, cube._5._3._1, cube._5._3._2), edge8);

        // 6 faces
        Set<NGMap.Dart> face1 = Sets.newHashSet(ngMap.iCell(cube._1._1._1, 2));
        assertEquals(Sets.newHashSet(cube._1._1._1, cube._1._1._2, cube._1._2._1, cube._1._2._2, cube._1._3._1,
                cube._1._3._2, cube._1._4._1, cube._1._4._2), face1);

        Set<NGMap.Dart> face2 = Sets.newHashSet(ngMap.iCell(cube._2._1._1, 2));
        assertEquals(Sets.newHashSet(cube._2._1._1, cube._2._1._2, cube._2._2._1, cube._2._2._2, cube._2._3._1,
                cube._2._3._2, cube._2._4._1, cube._2._4._2), face2);

        Set<NGMap.Dart> face3 = Sets.newHashSet(ngMap.iCell(cube._3._1._1, 2));
        assertEquals(Sets.newHashSet(cube._3._1._1, cube._3._1._2, cube._3._2._1, cube._3._2._2, cube._3._3._1,
                cube._3._3._2, cube._3._4._1, cube._3._4._2), face3);

        Set<NGMap.Dart> face4 = Sets.newHashSet(ngMap.iCell(cube._4._1._1, 2));
        assertEquals(Sets.newHashSet(cube._4._1._1, cube._4._1._2, cube._4._2._1, cube._4._2._2, cube._4._3._1,
                cube._4._3._2, cube._4._4._1, cube._4._4._2), face4);

        Set<NGMap.Dart> face5 = Sets.newHashSet(ngMap.iCell(cube._5._1._1, 2));
        assertEquals(Sets.newHashSet(cube._5._1._1, cube._5._1._2, cube._5._2._1, cube._5._2._2, cube._5._3._1,
                cube._5._3._2, cube._5._4._1, cube._5._4._2), face5);

        Set<NGMap.Dart> face6 = Sets.newHashSet(ngMap.iCell(cube._6._1._1, 2));
        assertEquals(Sets.newHashSet(cube._6._1._1, cube._6._1._2, cube._6._2._1, cube._6._2._2, cube._6._3._1,
                cube._6._3._2, cube._6._4._1, cube._6._4._2), face6);

    }

    @Test
    public void pyramidHas5Vertices8Edges5Faces() {
        NGMap<Object> ngMap = new NGMap<>(3);
        Pyramid pyramid = addPyramid(ngMap);

        // 5 vertices
        Set<NGMap.Dart> vertex1 = Sets.newHashSet(ngMap.iCell(pyramid.square._1._1, 0));
        assertEquals(Sets.newHashSet(pyramid.square._1._1, pyramid.square._4._2, pyramid.triangle1._1._1,
                pyramid.triangle1._3._2, pyramid.triangle4._1._2, pyramid.triangle4._2._1), vertex1);

        Set<NGMap.Dart> vertex2 = Sets.newHashSet(ngMap.iCell(pyramid.square._1._2, 0));
        assertEquals(Sets.newHashSet(pyramid.square._1._2, pyramid.square._2._1, pyramid.triangle1._2._2,
                pyramid.triangle1._3._1, pyramid.triangle2._1._2, pyramid.triangle2._2._1), vertex2);

        Set<NGMap.Dart> vertex3 = Sets.newHashSet(ngMap.iCell(pyramid.square._2._2, 0));
        assertEquals(Sets.newHashSet(pyramid.square._2._2, pyramid.square._3._1, pyramid.triangle2._1._1,
                pyramid.triangle2._3._2, pyramid.triangle3._1._2, pyramid.triangle3._2._1), vertex3);

        Set<NGMap.Dart> vertex4 = Sets.newHashSet(ngMap.iCell(pyramid.square._3._2, 0));
        assertEquals(Sets.newHashSet(pyramid.square._3._2, pyramid.square._4._1, pyramid.triangle3._1._1,
                pyramid.triangle3._3._2, pyramid.triangle4._2._2, pyramid.triangle4._3._1), vertex4);

        Set<NGMap.Dart> vertex5 = Sets.newHashSet(ngMap.iCell(pyramid.triangle1._1._2, 0));
        assertEquals(Sets.newHashSet(pyramid.triangle1._1._2, pyramid.triangle1._2._1, pyramid.triangle2._2._2,
                pyramid.triangle2._3._1, pyramid.triangle3._2._2, pyramid.triangle3._3._1, pyramid.triangle4._1._1,
                pyramid.triangle4._3._2), vertex5);

        // 8 edges
        Set<NGMap.Dart> edge1 = Sets.newHashSet(ngMap.iCell(pyramid.square._1._1, 1));
        assertEquals(Sets.newHashSet(pyramid.square._1._1, pyramid.square._1._2, pyramid.triangle1._3._1,
                pyramid.triangle1._3._2), edge1);

        Set<NGMap.Dart> edge2 = Sets.newHashSet(ngMap.iCell(pyramid.square._2._1, 1));
        assertEquals(Sets.newHashSet(pyramid.square._2._1, pyramid.square._2._2, pyramid.triangle2._1._1,
                pyramid.triangle2._1._2), edge2);

        Set<NGMap.Dart> edge3 = Sets.newHashSet(ngMap.iCell(pyramid.square._3._1, 1));
        assertEquals(Sets.newHashSet(pyramid.square._3._1, pyramid.square._3._2, pyramid.triangle3._1._1,
                pyramid.triangle3._1._2), edge3);

        Set<NGMap.Dart> edge4 = Sets.newHashSet(ngMap.iCell(pyramid.square._4._1, 1));
        assertEquals(Sets.newHashSet(pyramid.square._4._1, pyramid.square._4._2, pyramid.triangle4._2._1,
                pyramid.triangle4._2._2), edge4);

        // 5 faces
        Set<NGMap.Dart> face1 = Sets.newHashSet(ngMap.iCell(pyramid.square._1._1, 2));
        assertEquals(Sets.newHashSet(pyramid.square._1._1, pyramid.square._1._2, pyramid.square._2._1,
                pyramid.square._2._2, pyramid.square._3._1, pyramid.square._3._2, pyramid.square._4._1,
                pyramid.square._4._2), face1);

        Set<NGMap.Dart> face2 = Sets.newHashSet(ngMap.iCell(pyramid.triangle1._1._1, 2));
        assertEquals(Sets.newHashSet(pyramid.triangle1._1._1, pyramid.triangle1._1._2, pyramid.triangle1._2._1,
                pyramid.triangle1._2._2, pyramid.triangle1._3._1, pyramid.triangle1._3._2), face2);

        Set<NGMap.Dart> face3 = Sets.newHashSet(ngMap.iCell(pyramid.triangle2._1._1, 2));
        assertEquals(Sets.newHashSet(pyramid.triangle2._1._1, pyramid.triangle2._1._2, pyramid.triangle2._2._1,
                pyramid.triangle2._2._2, pyramid.triangle2._3._1, pyramid.triangle2._3._2), face3);

        Set<NGMap.Dart> face4 = Sets.newHashSet(ngMap.iCell(pyramid.triangle3._1._1, 2));
        assertEquals(Sets.newHashSet(pyramid.triangle3._1._1, pyramid.triangle3._1._2, pyramid.triangle3._2._1,
                pyramid.triangle3._2._2, pyramid.triangle3._3._1, pyramid.triangle3._3._2), face4);

        Set<NGMap.Dart> face5 = Sets.newHashSet(ngMap.iCell(pyramid.triangle4._1._1, 2));
        assertEquals(Sets.newHashSet(pyramid.triangle4._1._1, pyramid.triangle4._1._2, pyramid.triangle4._2._1,
                pyramid.triangle4._2._2, pyramid.triangle4._3._1, pyramid.triangle4._3._2), face5);
    }

    @Test
    public void houseIsCubePlusPyramidVia4Vertices4Edges2Faces() {
        NGMap<Object> ngMap = new NGMap<>(3);
        House house = addHouse(ngMap);

        // We assert that vertices, edges, and faces are valid for the cube and pyramid independently, here we assert
        // validity of the vertices, edges, and faces of both volumes, i.e. the intersection

        // 4 vertices
        Set<NGMap.Dart> vertex1 = Sets.newHashSet(ngMap.iCell(house.cube._1._1._1, 0));
        assertEquals(ImmutableSet.builder()
                .addAll(ngMap.iCell(house.cube._1._1._1, 0))
                .addAll(ngMap.iCell(house.pyramid.square._1._1, 0))
                .build(), vertex1);

        Set<NGMap.Dart> vertex2 = Sets.newHashSet(ngMap.iCell(house.cube._1._1._2, 0));
        assertEquals(ImmutableSet.builder()
                .addAll(ngMap.iCell(house.cube._1._1._2, 0))
                .addAll(ngMap.iCell(house.pyramid.square._1._2, 0))
                .build(), vertex2);

        Set<NGMap.Dart> vertex3 = Sets.newHashSet(ngMap.iCell(house.cube._1._2._1, 0));
        assertEquals(ImmutableSet.builder()
                .addAll(ngMap.iCell(house.cube._1._2._1, 0))
                .addAll(ngMap.iCell(house.pyramid.square._2._1, 0))
                .build(), vertex3);

        Set<NGMap.Dart> vertex4 = Sets.newHashSet(ngMap.iCell(house.cube._1._2._2, 0));
        assertEquals(ImmutableSet.builder()
                .addAll(ngMap.iCell(house.cube._1._2._2, 0))
                .addAll(ngMap.iCell(house.pyramid.square._2._2, 0))
                .build(), vertex4);

        // 4 edges
        Set<NGMap.Dart> edge1 = Sets.newHashSet(ngMap.iCell(house.cube._1._1._1, 1));
        assertEquals(ImmutableSet.builder()
                .addAll(ngMap.iCell(house.cube._1._1._1, 1))
                .addAll(ngMap.iCell(house.pyramid.square._1._1, 1)).build(), edge1);

        Set<NGMap.Dart> edge2 = Sets.newHashSet(ngMap.iCell(house.cube._1._2._1, 1));
        assertEquals(ImmutableSet.builder()
                .addAll(ngMap.iCell(house.cube._1._2._1, 1))
                .addAll(ngMap.iCell(house.pyramid.square._2._1, 1))
                .build(), edge2);

        Set<NGMap.Dart> edge3 = Sets.newHashSet(ngMap.iCell(house.cube._1._3._1, 1));
        assertEquals(ImmutableSet.builder()
                .addAll(ngMap.iCell(house.cube._1._3._1, 1))
                .addAll(ngMap.iCell(house.pyramid.square._3._1, 1))
                .build(), edge3);

        Set<NGMap.Dart> edge4 = Sets.newHashSet(ngMap.iCell(house.cube._1._4._1, 1));
        assertEquals(ImmutableSet.builder()
                .addAll(ngMap.iCell(house.cube._1._4._1, 1))
                .addAll(ngMap.iCell(house.pyramid.square._4._1, 1))
                .build(), edge4);

        // 1 face
        Set<NGMap.Dart> face1 = Sets.newHashSet(ngMap.iCell(house.cube._1._1._1, 2));
        assertEquals(ImmutableSet.builder()
                .addAll(ngMap.iCell(house.cube._1._1._1, 2))
                .addAll(ngMap.iCell(house.pyramid.square._1._1, 2))
                .build(), face1);
    }

    @Test
    public void canMakeACylinder() {
        NGMap<Object> ngMap = new NGMap<>(2);
        Square square = addSquare(ngMap);

        ngMap.sew(square._2._1, square._4._2, 2);

        // 2 vertices
        Set<NGMap.Dart> vertex1 = Sets.newHashSet(ngMap.iCell(square._1._1, 0));
        assertEquals(Sets.newHashSet(square._1._1, square._4._2, square._1._2, square._2._1), vertex1);

        Set<NGMap.Dart> vertex2 = Sets.newHashSet(ngMap.iCell(square._3._1, 0));
        assertEquals(Sets.newHashSet(square._3._1, square._2._2, square._3._2, square._4._1), vertex2);

        // Only 1 face with all the darts
        Set<NGMap.Dart> face1 = Sets.newHashSet(ngMap.iCell(square._1._1, 2));
        assertEquals(Sets.newHashSet(square._1._1, square._1._2, square._2._1, square._2._2, square._3._1, square._3._2,
                square._4._1, square._4._2), face1);

    }

    @Test(expected = IllegalStateException.class)
    public void cantIdentifyEdgeWithItself() {
        NGMap<Object> ngMap = new NGMap<>(2);
        Square square = addSquare(ngMap);

        ngMap.sew(square._1._1, square._1._2, 2);
    }

    /*
     * On page 7 of Cellular Modeling in Arbitrary Dimension using Generalized Maps, Le ́vy + Mallet, they describe
     * a constraint placed on the n-GMap to avoid identification of two adjacent cells as
     *   ∀0 ≤ i < i + 2 ≤ j ≤ N, the function αi o αj has no fixed point.
     * However, I don't know how to enforce this within the sew operation.
     */
    @Test//(expected = IllegalStateException.class
    public void cantFoldEdge() {
        /* We're going to construct a square in which one age contains 6 darts
         *
         *    *---------------e1 ---------------*
         *   *                                   *
         *   |                                   |
         *  e7                                  e2
         *   |                                   |
         *   *                                   *
         *    *--e6--* *--e5--* *--e4--* *--e3--*
         */

        NGMap<Object> ngMap = new NGMap<>(2);
        Edge edge1 = NGMapUtils.addEdge(ngMap);
        Edge edge2 = NGMapUtils.addEdge(ngMap);
        Edge edge3 = NGMapUtils.addEdge(ngMap);
        Edge edge4 = NGMapUtils.addEdge(ngMap);
        Edge edge5 = NGMapUtils.addEdge(ngMap);
        Edge edge6 = NGMapUtils.addEdge(ngMap);
        Edge edge7 = NGMapUtils.addEdge(ngMap);

        ngMap.sew(edge1._1, edge7._2, 1);
        ngMap.sew(edge2._1, edge1._2, 1);
        ngMap.sew(edge3._1, edge2._2, 1);
        ngMap.sew(edge4._1, edge3._2, 1);
        ngMap.sew(edge5._1, edge4._2, 1);
        ngMap.sew(edge6._1, edge5._2, 1);
        ngMap.sew(edge7._1, edge6._2, 1);

        // Should be prohibited
        ngMap.sew(edge4._2, edge5._1, 2);
    }

    @Test
    public void canPutAndGetAttribute() {
        NGMap<Object> intNGMap = new NGMap<>(2);
        Square square = addSquare(intNGMap);
        intNGMap.putAttribute(square._1._1, 0, new StringAttr(VERTEX_1));
        intNGMap.putAttribute(square._2._1, 0, new StringAttr(VERTEX_2));
        intNGMap.putAttribute(square._3._1, 0, new StringAttr(VERTEX_3));
        intNGMap.putAttribute(square._4._1, 0, new StringAttr(VERTEX_4));

        intNGMap.putAttribute(square._1._1, 1, new StringAttr(EDGE_1));
        intNGMap.putAttribute(square._2._1, 1, new StringAttr(EDGE_2));
        intNGMap.putAttribute(square._3._1, 1, new StringAttr(EDGE_3));
        intNGMap.putAttribute(square._4._1, 1, new StringAttr(EDGE_4));

        assertEquals(VERTEX_1, intNGMap.getAttribute(square._1._1, 0).get());
        assertEquals(VERTEX_1, intNGMap.getAttribute(square._4._2, 0).get());

        assertEquals(VERTEX_2, intNGMap.getAttribute(square._2._1, 0).get());
        assertEquals(VERTEX_2, intNGMap.getAttribute(square._1._2, 0).get());

        assertEquals(VERTEX_3, intNGMap.getAttribute(square._3._1, 0).get());
        assertEquals(VERTEX_3, intNGMap.getAttribute(square._2._2, 0).get());

        assertEquals(VERTEX_4, intNGMap.getAttribute(square._4._1, 0).get());
        assertEquals(VERTEX_4, intNGMap.getAttribute(square._3._2, 0).get());


        assertEquals(EDGE_1, intNGMap.getAttribute(square._1._1, 1).get());
        assertEquals(EDGE_1, intNGMap.getAttribute(square._1._1, 1).get());

        assertEquals(EDGE_2, intNGMap.getAttribute(square._2._1, 1).get());
        assertEquals(EDGE_2, intNGMap.getAttribute(square._2._2, 1).get());

        assertEquals(EDGE_3, intNGMap.getAttribute(square._3._1, 1).get());
        assertEquals(EDGE_3, intNGMap.getAttribute(square._3._2, 1).get());

        assertEquals(EDGE_4, intNGMap.getAttribute(square._4._1, 1).get());
        assertEquals(EDGE_4, intNGMap.getAttribute(square._4._2, 1).get());
    }

    @Test
    public void canPutAndRemoveAttribute() {
        NGMap<Object> intNGMap = new NGMap<>(2);
        Square square = addSquare(intNGMap);

        intNGMap.putAttribute(square._1._1, 0, new StringAttr(VERTEX_1));
        assertEquals(VERTEX_1, intNGMap.getAttribute(square._1._1, 0).get());
        assertEquals(VERTEX_1, intNGMap.getAttribute(square._4._2, 0).get());
        intNGMap.removeAttribute(square._1._1, 0);
        assertEquals(Optional.empty(), intNGMap.getAttribute(square._1._1, 0));
        assertEquals(Optional.empty(), intNGMap.getAttribute(square._4._2, 0));

        intNGMap.putAttribute(square._1._1, 0, new StringAttr(VERTEX_1));
        assertEquals(VERTEX_1, intNGMap.getAttribute(square._1._1, 0).get());
        assertEquals(VERTEX_1, intNGMap.getAttribute(square._4._2, 0).get());
        intNGMap.removeAttribute(square._4._2, 0);
        assertEquals(Optional.empty(), intNGMap.getAttribute(square._1._1, 0));
        assertEquals(Optional.empty(), intNGMap.getAttribute(square._4._2, 0));
    }

    @Test(expected = IllegalStateException.class)
    public void cantOverwriteAttribute() {
        NGMap<Object> intNGMap = new NGMap<>(2);
        Square square = addSquare(intNGMap);

        intNGMap.putAttribute(square._1._1, 0, new StringAttr(VERTEX_1));
        intNGMap.putAttribute(square._4._2, 0, new StringAttr(VERTEX_1));
    }

    @Value
    private static class StringAttr implements Attribute {
        private final String string;
    }

    private Triangle addTriangle(NGMap<Object> ngMap) {
        Edge edge1 = NGMapUtils.addEdge(ngMap);
        Edge edge2 = NGMapUtils.addEdge(ngMap);
        Edge edge3 = NGMapUtils.addEdge(ngMap);

        ngMap.sew(edge1._1, edge3._2, 1);
        ngMap.sew(edge2._1, edge1._2, 1);
        ngMap.sew(edge3._1, edge2._2, 1);

        return new Triangle(edge1, edge2, edge3);
    }

    /*
     *       d1     d2
     *        *--e1--*
     *    d8 *        * d3
     *       |        |
     *      e4       e2
     *       |        |
     *    d7 *        * d4
     *        *--e3--*
     *       d6      d5
     */
    private Square addSquare(NGMap<Object> ngMap) {
        Edge edge1 = NGMapUtils.addEdge(ngMap);
        Edge edge2 = NGMapUtils.addEdge(ngMap);
        Edge edge3 = NGMapUtils.addEdge(ngMap);
        Edge edge4 = NGMapUtils.addEdge(ngMap);

        ngMap.sew(edge1._1, edge4._2, 1);
        ngMap.sew(edge2._1, edge1._2, 1);
        ngMap.sew(edge3._1, edge2._2, 1);
        ngMap.sew(edge4._1, edge3._2, 1);

        return new Square(edge1, edge2, edge3, edge4);
    }

    /*
     *
     *        *--e0--*    *--e0--*
     *       *        *  *        *
     *       |        |  |        |
     *      e3   f0  e1 e3   f2  e1
     *       |        |  |        |
     *       *        *  *        *
     *        *--e2--*    *--e2--*
     *
     */
    private Cube addCube(NGMap<Object> ngMap) {
        Square square1 = addSquare(ngMap);
        Square square2 = addSquare(ngMap);
        Square square3 = addSquare(ngMap);
        Square square4 = addSquare(ngMap);
        Square square5 = addSquare(ngMap);
        Square square6 = addSquare(ngMap);

        ngMap.sew(square1._1._1, square2.get_3()._2, 2);
        ngMap.sew(square1._2._1, square3.get_4()._2, 2);
        ngMap.sew(square1.get_3()._1, square4._1._2, 2);
        ngMap.sew(square1.get_4()._1, square5._2._2, 2);

        ngMap.sew(square2._1._1, square6.get_3()._2, 2);
        ngMap.sew(square2._2._1, square3._1._2, 2);
        ngMap.sew(square2.get_4()._1, square5._1._2, 2);

        ngMap.sew(square3._2._1, square6._2._2, 2);
        ngMap.sew(square3.get_3()._1, square4._2._2, 2);

        ngMap.sew(square4.get_3()._1, square6._1._2, 2);
        ngMap.sew(square4.get_4()._1, square5.get_3()._2, 2);

        ngMap.sew(square5.get_4()._1, square6.get_4()._2, 2);

        return new Cube(square1, square2, square3, square4, square5, square6);
    }

    /*
     *
     */
    private Pyramid addPyramid(NGMap<Object> ngMap) {
        Square square = addSquare(ngMap);
        Triangle triangle1 = addTriangle(ngMap);
        Triangle triangle2 = addTriangle(ngMap);
        Triangle triangle3 = addTriangle(ngMap);
        Triangle triangle4 = addTriangle(ngMap);

        ngMap.sew(square._1._1, triangle1.get_3()._2, 2);
        ngMap.sew(square._2._1, triangle2._1._2, 2);
        ngMap.sew(square.get_3()._1, triangle3._1._2, 2);
        ngMap.sew(square.get_4()._1, triangle4._2._2, 2);

        ngMap.sew(triangle1._1._1, triangle4._1._2, 2);
        ngMap.sew(triangle1._2._1, triangle2._2._2, 2);

        ngMap.sew(triangle2.get_3()._1, triangle3._2._2, 2);

        ngMap.sew(triangle3.get_3()._1, triangle4.get_3()._2, 2);

        return new Pyramid(square, triangle1, triangle2, triangle3, triangle4);
    }

    private House addHouse(NGMap<Object> ngMap) {
        Cube cube = addCube(ngMap);
        Pyramid pyramid = addPyramid(ngMap);

        // Only need to sew dart to sew the volumes together!
        ngMap.sew(cube._1._1._1, pyramid.square._1._1, 3);

        return new House(cube, pyramid);
    }


    @Data
    private static final class Triangle {
        private final Edge _1;
        private final Edge _2;
        private final Edge _3;
    }

    @Data
    private static final class Square {
        private final Edge _1;
        private final Edge _2;
        private final Edge _3;
        private final Edge _4;
    }

    @Data
    private static final class Pyramid {
        private final Square square;
        private final Triangle triangle1;
        private final Triangle triangle2;
        private final Triangle triangle3;
        private final Triangle triangle4;
    }

    @Data
    private static final class Cube {
        private final Square _1;
        private final Square _2;
        private final Square _3;
        private final Square _4;
        private final Square _5;
        private final Square _6;
    }

    @Data
    private static final class House {
        private final Cube cube;
        private final Pyramid pyramid;
    }


}
