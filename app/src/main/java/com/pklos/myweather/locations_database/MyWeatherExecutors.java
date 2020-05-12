package com.pklos.myweather.locations_database;

import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MyWeatherExecutors {
    private static final Object LOCK = new Object();
    private static MyWeatherExecutors instance;
    private final Executor diskIO;
    private final Executor mainThread;
    private final Executor networkIO;

    private MyWeatherExecutors(Executor diskIO, Executor mainThread, Executor networkIO){
        this.diskIO = diskIO;
        this.mainThread = mainThread;
        this.networkIO = networkIO;
    }

    public static MyWeatherExecutors getInstance(){
        if(instance == null){
            synchronized (LOCK){
                instance = new MyWeatherExecutors(Executors.newSingleThreadExecutor(),
                                                  new MainThreadExecutor(), Executors.newFixedThreadPool(3));
            }
        }

        return instance;
    }

    public Executor getDiskIO() {
        return diskIO;
    }

    public Executor getMainThread() {
        return mainThread;
    }

    public Executor getNetworkIO() {
        return networkIO;
    }

    private static class MainThreadExecutor implements Executor{
//        private Handler mainThreadHandler = new Handler() {
//            @Override
//            public void publish(LogRecord record) {
//
//            }
//
//            @Override
//            public void flush() {
//
//            }
//
//            @Override
//            public void close() throws SecurityException {
//
//            }
//        };

        @Override
        public void execute(@NonNull Runnable command) {
           // mainThreadHandler.post(command);
        }
    }
}
