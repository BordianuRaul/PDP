package org.example;

import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {
        ArrayList<Integer> first = new ArrayList<>();
        first.add(1);
        first.add(7);
        first.add(121);
        first.add(19);
        first.add(3);

        ArrayList<Integer> second = new ArrayList<>();

        second.add(2);
        second.add(2);
        second.add(2);
        second.add(2);
        second.add(2);

        Buffer buffer = new Buffer();
        Producer producer = new Producer(buffer, first, second);
        Consumer consumer = new Consumer(buffer, first.size());

        producer.start();
        consumer.start();

        //expected result 302
    }
}