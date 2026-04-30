package com.yader;

import java.util.List;
import java.util.Random;

/**
 * Утилита для измерения производительности базовых операций над {@link List}.
 * <p>
 * Каждый метод {@code benchmark*} выполняет фиксированное число операций
 * над переданной коллекцией и возвращает время выполнения в миллисекундах.
 * Перед каждым замером список приводится к нужному исходному состоянию.
 * <p>
 * Источник случайных чисел инициализируется с фиксированным сидом — так
 * результаты сравнения {@code ArrayList} и {@code LinkedList} строятся
 * на одной и той же последовательности индексов.
 */
public class ListBenchmark {

    /** Коллекция, над которой проводятся замеры. */
    private final List<Integer> targetList;

    /** Количество операций в одном замере. */
    private final int operationCount;

    /** Источник псевдослучайных индексов с фиксированным сидом. */
    private final Random random;

    /**
     * Создаёт новый бенчмарк для указанной коллекции.
     *
     * @param targetList     коллекция, для которой будут замеряться операции
     *                       (на момент создания должна быть пустой)
     * @param operationCount количество операций в одном замере; должно быть положительным
     * @throws IllegalArgumentException если {@code operationCount} не положителен
     */
    public ListBenchmark(List<Integer> targetList, int operationCount) {
        if (operationCount <= 0) {
            throw new IllegalArgumentException("operationCount must be positive");
        }
        this.targetList = targetList;
        this.operationCount = operationCount;
        this.random = new Random(42L);
    }

    /**
     * Замеряет добавление {@code operationCount} элементов в конец пустого списка.
     *
     * @return время выполнения серии операций в миллисекундах
     */
    public double benchmarkAddToEnd() {
        targetList.clear();
        long startNs = System.nanoTime();
        for (int i = 0; i < operationCount; i++) {
            targetList.add(i);
        }
        return toMillis(System.nanoTime() - startNs);
    }

    /**
     * Замеряет получение элемента по случайному индексу.
     * Перед замером список заполняется значениями {@code 0..operationCount-1}.
     *
     * @return время выполнения серии операций в миллисекундах
     */
    public double benchmarkGetByIndex() {
        fillList();
        long startNs = System.nanoTime();
        for (int i = 0; i < operationCount; i++) {
            targetList.get(random.nextInt(operationCount));
        }
        return toMillis(System.nanoTime() - startNs);
    }

    /**
     * Замеряет удаление первого элемента списка ({@code remove(0)}).
     *
     * @return время выполнения серии операций в миллисекундах
     */
    public double benchmarkRemoveFromHead() {
        fillList();
        long startNs = System.nanoTime();
        for (int i = 0; i < operationCount; i++) {
            targetList.remove(0);
        }
        return toMillis(System.nanoTime() - startNs);
    }

    /**
     * Замеряет вставку элемента по случайной позиции в уже заполненный список.
     * После замера размер списка удваивается.
     *
     * @return время выполнения серии операций в миллисекундах
     */
    public double benchmarkInsertAtRandom() {
        fillList();
        long startNs = System.nanoTime();
        for (int i = 0; i < operationCount; i++) {
            int index = random.nextInt(targetList.size() + 1);
            targetList.add(index, -1);
        }
        return toMillis(System.nanoTime() - startNs);
    }

    /**
     * Замеряет удаление элементов по случайной позиции до полного опустошения списка.
     *
     * @return время выполнения серии операций в миллисекундах
     */
    public double benchmarkRemoveAtRandom() {
        fillList();
        long startNs = System.nanoTime();
        for (int i = 0; i < operationCount; i++) {
            targetList.remove(random.nextInt(targetList.size()));
        }
        return toMillis(System.nanoTime() - startNs);
    }

    /**
     * Замеряет линейный поиск элемента в списке методом {@code contains}.
     *
     * @return время выполнения серии операций в миллисекундах
     */
    public double benchmarkContains() {
        fillList();
        long startNs = System.nanoTime();
        for (int i = 0; i < operationCount; i++) {
            targetList.contains(random.nextInt(operationCount * 2));
        }
        return toMillis(System.nanoTime() - startNs);
    }

    /**
     * Замеряет замену элемента ({@code set}) по случайному индексу.
     *
     * @return время выполнения серии операций в миллисекундах
     */
    public double benchmarkSetAtRandom() {
        fillList();
        long startNs = System.nanoTime();
        for (int i = 0; i < operationCount; i++) {
            targetList.set(random.nextInt(targetList.size()), i);
        }
        return toMillis(System.nanoTime() - startNs);
    }

    /**
     * Возвращает простое имя класса тестируемой коллекции (например {@code "ArrayList"}).
     *
     * @return имя класса коллекции для отображения в таблице результатов
     */
    public String getListTypeName() {
        return targetList.getClass().getSimpleName();
    }

    /** Заполняет список значениями {@code 0..operationCount-1}. */
    private void fillList() {
        targetList.clear();
        for (int i = 0; i < operationCount; i++) {
            targetList.add(i);
        }
    }

    /** Конвертирует наносекунды в миллисекунды. */
    private static double toMillis(long nanos) {
        return nanos / 1_000_000.0;
    }
}