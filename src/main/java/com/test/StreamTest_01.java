package com.test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamTest_01 {
    public static void main(String[] args) {
        Stream<Integer> stream = Stream.of(1, 2, 3, null, 4, 5, 6, 7);
        stream.filter(num -> num != null && num > 4)
                .peek(num1 -> System.out.println("first : " + num1))
                .filter(num -> num / 2 != 1)
                .peek(num -> System.out.println("second : " + num))
                .mapToLong(num -> num * 2)
                .peek(num -> System.out.println("third : " + num))
                .sum();
//        System.out.println(sum);


//        List<String> versions = new ArrayList<>();
//        versions.add("Lollipop");
//        versions.add("KitKat");
//        versions.add("Jelly Bean");
//        versions.add("Ice Cream Sandwidch");
//        versions.add("Honeycomb");
//        versions.add("Gingerbread");
//        // filtering all vaersion which are longer than 7 characters
//        versions.stream().filter(s -> s.length() > 7)
//                .peek(e -> System.out.println("After the first filter: " + e))
//                .filter(s -> s.startsWith("H"))
//                .peek(e -> System.out.println("After the second filter: " + e))
//                .collect(Collectors.toSet());

    }
}
