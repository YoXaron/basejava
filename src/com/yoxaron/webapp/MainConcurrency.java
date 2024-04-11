package com.yoxaron.webapp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainConcurrency {

//    private static final Object LOCK = new Object();
    private static final Lock LOCK = new ReentrantLock();
    public static final int THREADS_NUMBER = 10000;
    private static int counter = 0;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public static void main(String[] args) throws InterruptedException {
        System.out.println(Thread.currentThread().getName());

        Thread thread0 = new Thread() {
            public void run() {
                System.out.println(getName() + ", " + getState());
            }
        };
        thread0.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + ", " + Thread.currentThread().getState());
            }
        }).start();

        System.out.println(thread0.getState());

        final MainConcurrency mainConcurrency = new MainConcurrency();
        CountDownLatch latch = new CountDownLatch(THREADS_NUMBER);
        int processors = Runtime.getRuntime().availableProcessors();
        System.out.println("Available procossors: " + processors);
        ExecutorService executorService = Executors.newFixedThreadPool(processors);
//        CompletionService completionService = new ExecutorCompletionService(executorService);
//        ExecutorService executorService = Executors.newCachedThreadPool();
//        List<Thread> threads = new ArrayList<>(THREADS_NUMBER);

        for (int i = 0; i < THREADS_NUMBER; i++) {
            Future<Integer> future = executorService.submit(() -> {
                for (int j = 0; j < 100; j++) {
                    mainConcurrency.increment();
                    LocalDateTime.now().format(formatter);
                }
                latch.countDown();
                return counter;
            });
//            completionService.poll();

//            Thread thread = new Thread(() -> {
//                for (int j = 0; j < 100; j++) {
//                    mainConcurrency.increment();
//                }
//                latch.countDown();
//            });
//            thread.start();
//            threads.add(thread);
        }

//        threads.forEach(t -> {
//            try {
//                t.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });

        latch.await(10, TimeUnit.SECONDS);
        executorService.shutdown();
        System.out.println(counter);
    }

    private void increment() {
        LOCK.lock();
        try {
            counter++;
        } finally {
            LOCK.unlock();
        }
    }
}
