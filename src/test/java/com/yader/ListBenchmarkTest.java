package com.yader;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для {@link ListBenchmark}.
 * Проверяют контракты методов: возврат неотрицательного времени,
 * корректное состояние списка после замеров, правильное имя коллекции
 * и валидацию аргументов конструктора.
 */
class ListBenchmarkTest {

    private static final int OPS = 100;

    @Test
    void rejectsNonPositiveOperationCount() {
        assertThrows(IllegalArgumentException.class,
                () -> new ListBenchmark(new ArrayList<>(), 0));
        assertThrows(IllegalArgumentException.class,
                () -> new ListBenchmark(new ArrayList<>(), -5));
    }

    @Test
    void getListTypeNameReturnsSimpleClassName() {
        assertEquals("ArrayList",
                new ListBenchmark(new ArrayList<>(), OPS).getListTypeName());
        assertEquals("LinkedList",
                new ListBenchmark(new LinkedList<>(), OPS).getListTypeName());
    }

    @Test
    void addToEndLeavesExpectedSize() {
        List<Integer> list = new ArrayList<>();
        new ListBenchmark(list, OPS).benchmarkAddToEnd();
        assertEquals(OPS, list.size());
    }

    @Test
    void removeFromHeadEmptiesList() {
        List<Integer> list = new LinkedList<>();
        new ListBenchmark(list, OPS).benchmarkRemoveFromHead();
        assertTrue(list.isEmpty());
    }

    @Test
    void getByIndexDoesNotMutateSize() {
        List<Integer> list = new ArrayList<>();
        new ListBenchmark(list, OPS).benchmarkGetByIndex();
        assertEquals(OPS, list.size());
    }

    @Test
    void setAtRandomPreservesSize() {
        List<Integer> list = new ArrayList<>();
        new ListBenchmark(list, OPS).benchmarkSetAtRandom();
        assertEquals(OPS, list.size());
    }

    @Test
    void insertAtRandomDoublesSize() {
        List<Integer> list = new ArrayList<>();
        new ListBenchmark(list, OPS).benchmarkInsertAtRandom();
        assertEquals(OPS * 2, list.size());
    }

    @Test
    void removeAtRandomEmptiesList() {
        List<Integer> list = new ArrayList<>();
        new ListBenchmark(list, OPS).benchmarkRemoveAtRandom();
        assertTrue(list.isEmpty());
    }

    @Test
    void containsDoesNotMutate() {
        List<Integer> list = new ArrayList<>();
        new ListBenchmark(list, OPS).benchmarkContains();
        assertEquals(OPS, list.size());
    }

    @Test
    void allBenchmarksReturnNonNegativeTime() {
        ListBenchmark bench = new ListBenchmark(new ArrayList<>(), OPS);
        assertAll(
                () -> assertTrue(bench.benchmarkAddToEnd()       >= 0),
                () -> assertTrue(bench.benchmarkGetByIndex()     >= 0),
                () -> assertTrue(bench.benchmarkRemoveFromHead() >= 0),
                () -> assertTrue(bench.benchmarkInsertAtRandom() >= 0),
                () -> assertTrue(bench.benchmarkRemoveAtRandom() >= 0),
                () -> assertTrue(bench.benchmarkContains()       >= 0),
                () -> assertTrue(bench.benchmarkSetAtRandom()    >= 0)
        );
    }
}