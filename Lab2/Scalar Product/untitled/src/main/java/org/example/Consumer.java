package org.example;

public class Consumer extends Thread {
    private final Buffer buffer;
    private final Integer length;
    private Integer sum;

    public Consumer(Buffer buffer, Integer length) {
        super("Consumer");
        this.buffer = buffer;
        this.length = length;
        this.sum = 0;
    }

    @Override
    public void run() {
        for(int i = 0; i < length; i++) {
            try {
                sum += buffer.get();
                System.out.println("Consumer sum: " + sum);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
