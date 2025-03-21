package parallelMatrixMultiplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelMatrixMultiplication {
    private static final int SIZE = 2000; // Tamanho da matriz
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors(); // Número de threads
//    private static final int NUM_THREADS = 8; // Número de threads
    private static final int[][] matrixA = new int[SIZE][SIZE];
    private static final int[][] matrixB = new int[SIZE][SIZE];
    private static final int[][] resultMatrix = new int[SIZE][SIZE];

    public static void main(String[] args) {
        initializeMatrices(); // Preenche as matrizes com valores aleatórios
        
//        MatrixMultiplicationTaskNo();

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        int rowsPerThread = SIZE / NUM_THREADS;
        
        for (int i = 0; i < NUM_THREADS; i++) {
            int startIndex = i * rowsPerThread;
            int endIndex = (i == NUM_THREADS - 1) ? SIZE : (i + 1) * rowsPerThread;
            System.out.println(startIndex + " - " + endIndex + "\n");
            executor.execute(new MatrixMultiplicationTask(startIndex, endIndex));
        }
        
        executor.shutdown();
        while (!executor.isTerminated()) {
            Thread.yield();
        }
        
        System.out.println("Multiplicação de matrizes concluída.");
    }

    private static void initializeMatrices() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                matrixA[i][j] = (int) (Math.random() * 10);
                matrixB[i][j] = (int) (Math.random() * 10);
            }
        }
    }

    public static void MatrixMultiplicationTaskNo(){    
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                for (int k = 0; k < SIZE; k++) {
                    resultMatrix[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }
    }
    
    private static class MatrixMultiplicationTask implements Runnable {
        private final int startRow, endRow;

        public MatrixMultiplicationTask(int startRow, int endRow) {
            this.startRow = startRow;
            this.endRow = endRow;
        }

        @Override
        public void run() {
            for (int i = startRow; i < endRow; i++) {
                for (int j = 0; j < SIZE; j++) {
                    for (int k = 0; k < SIZE; k++) {
                        resultMatrix[i][j] += matrixA[i][k] * matrixB[k][j];
                    }
                }
            }
        }
    }
}
