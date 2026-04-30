package com.yader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.ToDoubleFunction;

/**
 * Точка входа программы. Запускает серию бенчмарков для {@link ArrayList}
 * и {@link LinkedList} и печатает сравнительную таблицу в консоль.
 */
public class Main {

    /** Количество операций в каждом замере. */
    private static final int OPERATIONS = 100_000;

    /**
     * Описание одного замера: отображаемое имя и функция, запускающая
     * соответствующий метод {@link ListBenchmark}.
     */
    private record BenchmarkCase(String name, ToDoubleFunction<ListBenchmark> action) {}

    public static void main(String[] args) {
        ListBenchmark arrayBench  = new ListBenchmark(new ArrayList<>(),  OPERATIONS);
        ListBenchmark linkedBench = new ListBenchmark(new LinkedList<>(), OPERATIONS);

        // Прогрев JVM: первые вызовы могут быть медленнее из-за JIT-компиляции,
        // поэтому сначала прогоняем несколько раз без учёта результата.
        warmUp(arrayBench, linkedBench);

        printHeader(arrayBench, linkedBench);

        List<BenchmarkCase> cases = List.of(
                new BenchmarkCase("add (end)",       ListBenchmark::benchmarkAddToEnd),
                new BenchmarkCase("add (random)",    ListBenchmark::benchmarkInsertAtRandom),
                new BenchmarkCase("get (random)",    ListBenchmark::benchmarkGetByIndex),
                new BenchmarkCase("set (random)",    ListBenchmark::benchmarkSetAtRandom),
                new BenchmarkCase("remove (head)",   ListBenchmark::benchmarkRemoveFromHead),
                new BenchmarkCase("remove (random)", ListBenchmark::benchmarkRemoveAtRandom),
                new BenchmarkCase("contains",        ListBenchmark::benchmarkContains)
        );

        for (BenchmarkCase c : cases) {
            double tArray  = c.action().applyAsDouble(arrayBench);
            double tLinked = c.action().applyAsDouble(linkedBench);
            System.out.printf("%-18s %-12d %-15.2f %-15.2f%n",
                    c.name(), OPERATIONS, tArray, tLinked);
        }
    }

    /** Несколько холостых прогонов, чтобы исключить влияние JIT-компиляции. */
    private static void warmUp(ListBenchmark a, ListBenchmark b) {
        for (int i = 0; i < 3; i++) {
            a.benchmarkAddToEnd();
            b.benchmarkAddToEnd();
        }
    }

    /** Печатает шапку таблицы с именами тестируемых коллекций. */
    private static void printHeader(ListBenchmark a, ListBenchmark b) {
        String h1 = a.getListTypeName()  + " (ms)";
        String h2 = b.getListTypeName() + " (ms)";
        System.out.printf("%-18s %-12s %-15s %-15s%n", "Method", "Operations", h1, h2);
        System.out.println("-".repeat(64));
    }
}