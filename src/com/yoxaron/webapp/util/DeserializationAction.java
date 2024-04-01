package com.yoxaron.webapp.util;

import java.io.IOException;

@FunctionalInterface
public interface DeserializationAction {
    void perform() throws IOException;
}
