package org.example;

import java.util.List;

public class Producer extends Thread {
    private final List<Integer> first;
    private final List<Integer> second;
    private final Buffer buffer;

    public Producer(Buffer buffer,List<Integer> first, List<Integer> second) {
        super("Producer");
        this.first = first;
        this.second = second;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        for(int i = 0; i < first.size(); i++) {
            try {
                System.out.println("Producer sending " + first.get(i) * second.get(i) + "\n");
                buffer.put(first.get(i) * second.get(i));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
