package com.yoxaron.webapp;

public class MainSingleton {

    private MainSingleton() {}

    private static final class MainSingletonHolder {
        private static final MainSingleton instance = new MainSingleton();
    }

    public synchronized static MainSingleton getInstance() {
        return MainSingletonHolder.instance;
    }
}