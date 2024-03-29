package com.yoxaron.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainFile {
    public static void main(String[] args) {
        String filePath = "./.gitignore";
        File file = new File(filePath);

        try {
            System.out.println(file.getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException("Error", e);
        }

        File dir = new File("./src/com/yoxaron/webapp");
        System.out.println(dir.isDirectory());

        String[] list = dir.list();
        if (list != null) {
            for (String name : list) {
                System.out.println(name);
            }
        }

        try (FileInputStream fis = new FileInputStream(filePath)) {
            while (fis.available() > 0) {
                System.out.print((char) fis.read());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
