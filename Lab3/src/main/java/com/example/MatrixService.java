package com.example;

import com.example.domain.Matrix;
import javafx.util.Pair;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MatrixService {

    private final static int NR_THREADS = 2;

    private Matrix result;
    int nrRows;
    int nrColumns;

    public MatrixService() {}

    public Matrix multiply(Matrix lhs, Matrix rhs) throws InterruptedException {
        result = new Matrix(lhs.getNrRows(), rhs.getNrColumns());
        nrRows = result.getNrRows();
        nrColumns = result.getNrColumns();

        multiplyTaskRowsSimpleThreads(lhs, rhs);
        return result;
    }

    private int computeSingleElement(List<Integer> row, List<Integer> column) {
        int result = 0;
        for(int i = 0; i < row.size(); i++) {
            result += row.get(i) * column.get(i);
        }
        return result;
    }

    private void computeRows(Matrix lhs, Matrix rhs, Pair<Integer, Integer> start, Pair<Integer, Integer> end) {
        if(start.getValue() != 0) {
            for(int column = start.getValue(); column < nrColumns; column++) {
                int row = start.getKey();
                result.setElement(row, column, computeSingleElement(lhs.getRow(row), rhs.getColumn(column)));
            }
            start = new Pair<>(start.getKey() + 1, start.getValue());
        }
        for(int row = start.getValue(); row < end.getKey(); row++) {
            for(int column = 0; column < nrColumns; column++) {
                result.setElement(row, column, computeSingleElement(lhs.getRow(row), rhs.getColumn(column)));
            }
        }
        int row = end.getKey();
        if(row < lhs.getNrRows()) {
            for (int column = 0; column < end.getValue(); column++) {
                result.setElement(row, column, computeSingleElement(lhs.getRow(row), rhs.getColumn(column)));
            }
        }
    }

    private void multiplyTaskRowsSimpleThreads(Matrix lhs, Matrix rhs) throws InterruptedException {
        int totalElements = nrRows * nrColumns;
        int elementsPerThread = totalElements / NR_THREADS;

        Thread[] threads = new Thread[NR_THREADS];
        for (int i = 0; i < NR_THREADS; i++) {
            int startRow = (i * elementsPerThread) / nrColumns;
            int startCol = (i * elementsPerThread) % nrColumns;
            int endRow = ((i + 1) * elementsPerThread) / nrColumns;
            int endCol = i != NR_THREADS - 1 ? ((i + 1) * elementsPerThread) % nrColumns : nrColumns;

            Pair<Integer, Integer> start = new Pair<>(startRow, startCol);
            Pair<Integer, Integer> end = new Pair<>(endRow, endCol);

            threads[i] = new Thread(() -> computeRows(lhs, rhs, start, end));
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }

    private void multiplyTaskRowsWithThreadPool(Matrix lhs, Matrix rhs) throws InterruptedException {
        int totalElements = nrRows * nrColumns;
        int elementsPerThread = totalElements / NR_THREADS;

        ExecutorService executor = Executors.newFixedThreadPool(NR_THREADS);

        try {
            for (int i = 0; i < NR_THREADS; i++) {
                int taskStartRow = (i * elementsPerThread) / nrColumns;
                int taskStartCol = (i * elementsPerThread) % nrColumns;
                int taskEndRow = ((i + 1) * elementsPerThread) / nrColumns;
                int taskEndCol = i != NR_THREADS - 1 ? ((i + 1) * elementsPerThread) % nrColumns : nrColumns;

                Pair<Integer, Integer> start = new Pair<>(taskStartRow, taskStartCol);
                Pair<Integer, Integer> end = new Pair<>(taskEndRow, taskEndCol);

                executor.submit(() -> computeRows(lhs, rhs, start, end));
            }
        } finally {
            executor.shutdown();
            if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                System.err.println("Executor did not terminate in the specified time.");
                executor.shutdownNow();
                if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                    System.err.println("Executor did not terminate after shutdownNow.");
                }
            }
        }
    }


}
