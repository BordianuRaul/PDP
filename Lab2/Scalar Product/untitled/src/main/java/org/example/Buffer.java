package org.example;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    private static final int BUFFER_SIZE = 2;
    private final Queue<Integer> buffer = new LinkedList<>();
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public Buffer() {
        //
    }

    public void put(int value) throws InterruptedException {
        lock.lock();
        try {
        } finally {
            lock.unlock();
            while (buffer.size() == BUFFER_SIZE) {
                System.out.println("Buffer is full, waiting...\n");
                condition.await();
            }

            buffer.add(value);
            System.out.println("Put " + value + " to buffer\n");

            lock.unlock();
        }
    }

    public int get() throws InterruptedException {
        lock.lock();
        try {
            while (buffer.isEmpty()) {
                System.out.println("Buffer is empty, waiting...\n");
                condition.await();
            }

            Integer value = buffer.poll();
            if(value != null) {
                System.out.println("Get " + value + " from buffer\n");

                condition.signal();

            }
            return value;
        } finally {
            lock.unlock();
        }
    }
}
