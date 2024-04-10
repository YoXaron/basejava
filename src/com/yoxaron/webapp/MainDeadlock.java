package com.yoxaron.webapp;

public class MainDeadlock {

    public static void main(String[] args) throws InterruptedException {
        final Object resource1 = new Object();
        final Object resource2 = new Object();

        Thread thread1 = new Thread(() -> execute(resource1, resource2));
        Thread thread2 = new Thread(() -> execute(resource2, resource1));

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("Main thread exiting...");
    }

    private static void execute(Object resource1, Object resource2) {
        synchronized (resource1) {
            System.out.println("Thread " + Thread.currentThread().getName() + ": Holding resource " + resource1 + "...");

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println("Thread " + Thread.currentThread().getName() + ": Waiting for resource " + resource2 + "...");

            synchronized (resource2) {
                System.out.println("Thread " + Thread.currentThread().getName() + ": Holding resource " + resource1 + " & " + resource2 + "...");
            }
        }
    }
}