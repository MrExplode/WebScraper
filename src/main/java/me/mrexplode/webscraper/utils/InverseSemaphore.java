package me.mrexplode.webscraper.utils;

public class InverseSemaphore {
    private int value = 0;
    private Object lock = new Object();

    public void taskStarted() {
        synchronized (lock) {
            value++;
        }
    }

    public void taskCompleted() {
        synchronized (lock) {
            value--;
            if (value == 0)
                lock.notifyAll();
        }
    }

    public void awaitCompletion() throws InterruptedException {
        synchronized (lock) {
            while (value > 0)
                lock.wait();
        }
    }
}
