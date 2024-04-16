package com.yoxaron.webapp;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainStream {

    public static void main(String[] args) {
        System.out.println(minValue(new int[]{1, 2, 3, 3, 2, 3, 7, 7, 1, 7}));
        System.out.println(oddOrEven(List.of(1, 1, 1, 1, 2, 1)));
    }

    public static int minValue(int[] values) {
        return Arrays.stream(values)
                .distinct()
                .sorted()
                .reduce(0, (acc, num) -> acc * 10 + num);
    }

    public static List<Integer> oddOrEven(List<Integer> integers) {
        return integers.stream()
                .collect(Collectors.partitioningBy(num -> num % 2 == 0))
                .get(integers.stream().mapToInt(Integer::intValue).sum() % 2 != 0);
    }
}