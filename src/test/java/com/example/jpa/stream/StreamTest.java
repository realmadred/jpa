package com.example.jpa.stream;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class StreamTest {

    List<Integer> list;

    /**
     * 生成一亿条0-100之间的记录
     */
    @Before
    public void init() {
        Random random = new Random();
        list = Stream.generate(() -> random.nextInt(100)).limit(100000000).collect(toList());
    }

    /**
     * tip
     */
    @org.junit.Test
    public void test1() {
        long begin1 = System.currentTimeMillis();
        list.stream().filter(x->(x > 10)).filter(x->x<80).count();
        long end1 = System.currentTimeMillis();
        System.out.println(end1-begin1);
        list.stream().parallel().filter(x->(x > 10)).filter(x->x<80).count();
        long end2 = System.currentTimeMillis();
        System.out.println(end2-end1);

        long begin1_ = System.currentTimeMillis();
        list.stream().filter(x->(x > 10)).filter(x->x<80).distinct().sorted().count();
        long end1_ = System.currentTimeMillis();
        System.out.println(end1_-begin1_);
        list.stream().parallel().filter(x->(x > 10)).filter(x->x<80).distinct().sorted().count();
        long end2_ = System.currentTimeMillis();
        System.out.println(end2_-end1_);

    }

    @Test
    public void test() {
        List<String> collect = Stream.of("one", "two", "three", "four")
                .filter(e -> e.length() > 3)
                .peek(e -> System.out.println("Filtered value: " + e))
                .map(String::toUpperCase)
                .peek(e -> System.out.println("Mapped value: " + e))
                .collect(toList());
        System.out.println(collect);
    }

    @Test
    public void test2() {
        long[] arrayOfLong = new long [20000];
        Arrays.parallelSetAll( arrayOfLong,
                index -> ThreadLocalRandom.current().nextInt( 1000000 ) );
        Arrays.stream( arrayOfLong ).limit( 10 ).forEach(
                i -> System.out.print( i + " " ) );
        System.out.println();

        Arrays.parallelSort( arrayOfLong );
        Arrays.stream( arrayOfLong ).limit( 10 ).forEach(
                i -> System.out.print( i + " " ) );
        System.out.println();
    }
}
