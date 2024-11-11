package com.example;

import com.example.domain.Matrix;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Matrix lhs = new Matrix(100, 100);
        Matrix rhs = new Matrix(100, 100);

        MatrixService service = new MatrixService();

        Matrix result = service.multiply(lhs, rhs);

        System.out.println(lhs);
        System.out.println("\n");
        System.out.println("X");
        System.out.println("\n");
        System.out.println(rhs);
        System.out.println("\n");
        System.out.println("=");
        System.out.println(result);
    }
}