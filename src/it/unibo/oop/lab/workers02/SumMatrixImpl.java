package it.unibo.oop.lab.workers02;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class SumMatrixImpl implements SumMatrix {

    private final int nthread;

    /**
     * 
     * @param nthread
     * number of treads performing the sum
     */
    public SumMatrixImpl(final int nthread) {
        this.nthread = nthread;
    }

    @Override
    public double sum(final double[][] matrix) {
        final int size = matrix.length / this.nthread + matrix.length % this.nthread;
        final List<Worker> workers = new ArrayList<>(nthread);
        for (int start = 0; start < matrix.length; start += size) {
            workers.add(new Worker(matrix, start, size));
        }
        for (final Thread worker : workers) {
            worker.start();
        }
        double sum = 0;
        for (final Worker worker: workers) {
            try {
                worker.join();
                sum += worker.getResult();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
        return sum;
    }

    private final class Worker extends Thread {
        private final double [][] matrix;
        private final int startPos;
        private final int nelem;
        private double result;

        private Worker(final double[][] matrix, final int startPos, final int nelem) {
            super();
            this.matrix = matrix;
            this.startPos = startPos;
            this.nelem = nelem;
         }

        public void run() {
            for (int i = startPos; i < matrix.length && i < startPos + nelem; i++) {
                for (final double d : this.matrix[i]) {
                    this.result += d;
                }
            }
        }

        public double getResult() {
            return this.result;
        }
    }
}
