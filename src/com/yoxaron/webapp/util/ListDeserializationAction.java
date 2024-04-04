package com.yoxaron.webapp.util;

import java.io.IOException;

public interface ListDeserializationAction<T> {
    T perform() throws IOException;
}