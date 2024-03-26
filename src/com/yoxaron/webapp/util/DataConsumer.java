package com.yoxaron.webapp.util;

import java.io.IOException;

@FunctionalInterface
public interface DataConsumer<T> {
    void accept(T t) throws IOException;
}