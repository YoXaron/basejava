package com.yoxaron.webapp;

import java.io.File;
import java.util.Arrays;

public class DirectoryTreePrinter {
    public static void main(String[] args) {
        String rootPath = "/Users/yoxaron/IdeaProjects/basejava";
        File rootDir = new File(rootPath);
        traverseDirectory(rootDir, 0);
    }

    public static void traverseDirectory(File directory, int depth) {
        if (!directory.isDirectory()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        Arrays.sort(files);

        for (File file : files) {
            if (file.getName().equals(".git")) continue;

            if (file.isDirectory()) {
                System.out.println(getIndent(depth) + file.getName() + "/");
                traverseDirectory(file, depth + 1);
            }
        }

        for (File file : files) {
            if (file.isFile()) {
                System.out.println(getIndent(depth) + file.getName());
            }
        }
    }

    private static String getIndent(int depth) {
        return "  ".repeat(depth);
    }
}
