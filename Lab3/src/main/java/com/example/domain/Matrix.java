package com.example.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Matrix {
    private List<List<Integer>> data;
    int nrRows;
    int nrColumns;

    public Matrix(int nrRows, int nrColumns) {
        this.nrRows = nrRows;
        this.nrColumns = nrColumns;

        Random rand = new Random();

        this.data = new ArrayList<>(nrRows);
        for(int i = 0; i < nrRows; i++) {
            this.data.add(new ArrayList<>(nrColumns));
            for(int j = 0; j < nrColumns; j++) {
                this.data.get(i).add(rand.nextInt(100));
            }
        }
    }

    public List<Integer> getRow(int row) {
        return this.data.get(row);
    }

    public List<Integer> getColumn(int columnIndex) {
        List<Integer> column = new ArrayList<>();

        for(List<Integer> row : data){
            column.add(row.get(columnIndex));
        }

        return column;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < nrRows; i++) {
            for(int j = 0; j < nrColumns; j++) {
                sb.append(data.get(i).get(j)).append(" ");
                if(j == nrColumns - 1) {
                    sb.append("\n");
                }
            }
        }
        return sb.toString();
    }

}
