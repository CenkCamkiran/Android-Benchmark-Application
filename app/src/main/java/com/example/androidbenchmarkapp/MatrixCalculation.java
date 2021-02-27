package com.example.androidbenchmarkapp;


import android.util.Log;

import java.util.Arrays;

public class MatrixCalculation {
    //7x7 lük 2 matris çarpımı (elemanları rastgele atanmış)
    private double[][] A = new double[7][7];
    private double[][] B = new double[7][7];

    public double start, finish;
    public double elapsedTime_Multip, elapsedTime_Inverse, elapsedTime_RRF, elapsedTime_Complex, elapsedTime_Equations;


    private double determinant(double[][] matrix) {
        if (matrix.length != matrix[0].length)
            throw new IllegalStateException("invalid dimensions");

        if (matrix.length == 2)
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];

        double det = 0;
        for (int i = 0; i < matrix[0].length; i++)
            det += Math.pow(-1, i) * matrix[0][i]
                    * determinant(minor(matrix, 0, i));
        return det;
    }


    private double[][] inverse(double[][] matrix) {
        double[][] inverse = new double[matrix.length][matrix.length];

        // minors and cofactors
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[i].length; j++)
                inverse[i][j] = Math.pow(-1, i + j)
                        * determinant(minor(matrix, i, j));

        // adjugate and determinant
        double det = 1.0 / determinant(matrix);
        for (int i = 0; i < inverse.length; i++) {
            for (int j = 0; j <= i; j++) {
                double temp = inverse[i][j];
                inverse[i][j] = inverse[j][i] * det;
                inverse[j][i] = temp * det;
            }
        }

        return inverse;
    }


    private double[][] minor(double[][] matrix, int row, int column) {
        double[][] minor = new double[matrix.length - 1][matrix.length - 1];

        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; i != row && j < matrix[i].length; j++)
                if (j != column)
                    minor[i < row ? i : i - 1][j < column ? j : j - 1] = matrix[i][j];
        return minor;
    }


    private double[][] multiply(double[][] a, double[][] b) {
        if (a[0].length != b.length)
            throw new IllegalStateException("invalid dimensions");

        double[][] matrix = new double[a.length][b[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                double sum = 0;
                for (int k = 0; k < a[i].length; k++)
                    sum += a[i][k] * b[k][j];
                matrix[i][j] = sum;
            }
        }

        return matrix;
    }


    private double[][] rref(double[][] matrix) {
        double[][] rref = new double[matrix.length][];
        for (int i = 0; i < matrix.length; i++)
            rref[i] = Arrays.copyOf(matrix[i], matrix[i].length);

        int r = 0;
        for (int c = 0; c < rref[0].length && r < rref.length; c++) {
            int j = r;
            for (int i = r + 1; i < rref.length; i++)
                if (Math.abs(rref[i][c]) > Math.abs(rref[j][c]))
                    j = i;
            if (Math.abs(rref[j][c]) < 0.00001)
                continue;

            double[] temp = rref[j];
            rref[j] = rref[r];
            rref[r] = temp;

            double s = 1.0 / rref[r][c];
            for (j = 0; j < rref[0].length; j++)
                rref[r][j] *= s;
            for (int i = 0; i < rref.length; i++) {
                if (i != r) {
                    double t = rref[i][c];
                    for (j = 0; j < rref[0].length; j++)
                        rref[i][j] -= t * rref[r][j];
                }
            }
            r++;
        }

        return rref;
    }


    private double[][] transpose(double[][] matrix) {
        double[][] transpose = new double[matrix[0].length][matrix.length];

        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[i].length; j++)
                transpose[j][i] = matrix[i][j];
        return transpose;
    }


    protected void Main() {

        double[][] a = { { 1, 1, 1 }, { 0, 2, 5 }, { 2, 5, -1 } };
        double[][] b = { { 6 }, { -4 }, { 27 } };
        double[][] matrix;

        for (int i = 0; i < 7; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                A[i][j] = (int) (Math.random() * 10);
            }
        }

        for (int i = 0; i < 7; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                B[i][j] = (int) (Math.random() * 10);
            }
        }


        start = System.nanoTime();
        multiply(A, B);
        finish = System.nanoTime();
        elapsedTime_Multip = (finish - start) / 1000000; //matris çarpımı için geçen süre ms


        start = System.nanoTime();
        inverse(A); //ya da B de yazılabilir
        finish = System.nanoTime();
        elapsedTime_Inverse = (finish - start) / 1000000; //matrisinin tersi için geçen süre ms


        // example 1 - solving a system of equations
        start = System.nanoTime();
        multiply(inverse(a), b);
        finish = System.nanoTime();
        elapsedTime_Equations = (finish - start) / 1000000;

        /*for (double[] i : matrix)
            Log.i("Equations", Arrays.toString(i));
        System.out.println();*/


        // example 2 - example 1 using reduced row echelon form
        a = new double[][]{ { 1, 1, 1, 6 }, { 0, 2, 5, -4 }, { 2, 5, -1, 27 } };
        start = System.nanoTime();
        rref(a);
        finish = System.nanoTime();
        elapsedTime_RRF = (finish - start) / 1000000;

        /*for (double[] i : matrix)
            Log.i("RRF!!", Arrays.toString(i));
        System.out.println();*/


        // example 3 - solving a normal equation for linear regression
        double[][] x = { { 2104, 5, 1, 45 }, { 1416, 3, 2, 40 },
                { 1534, 3, 2, 30 }, { 852, 2, 1, 36 } };
        double[][] y = { { 460 }, { 232 }, { 315 }, { 178 } };

        start = System.nanoTime();
        matrix = multiply(
                multiply(inverse(multiply(transpose(x), x)), transpose(x)), y);
        finish = System.nanoTime();
        elapsedTime_Complex = (finish - start) / 1000000;
        /*for (double[] i : matrix)
            Log.i("Karmaşık İşlem", Arrays.toString(i));*/
    }
}
