package com.yoxaron.webapp;

import com.yoxaron.webapp.model.Resume;

import java.lang.reflect.Method;

public class MainReflection {
    public static void main(String[] args) throws Exception {
        Resume resume = new Resume("Test uuid");
        Class<?> clazz = resume.getClass();
        Method toString = clazz.getDeclaredMethod("toString");
        System.out.println(toString.invoke(resume));
    }
}
