package com.example.jpa.reactor;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

public class ReactorTest {

    @Test
    public void test() throws InterruptedException {
        Flux<Integer> just = Flux.just(1, 2, 3, 4, 5, 6);
        just.toStream().forEach(System.out::println);
        Mono<Integer> mono = Mono.just(1);

        //
        Flux.just("Hello", "World").subscribe(System.out::println);
        Flux.fromArray(new Integer[]{1, 2, 3}).subscribe(System.out::println);
        Flux.empty().subscribe(System.out::println);
        Flux.range(1, 10).subscribe(System.out::println);
        Flux.interval(Duration.of(10, ChronoUnit.MILLIS)).subscribe(System.out::println);

        TimeUnit.SECONDS.sleep(3);
    }

    @Test
    public void test1() {
        Integer[] array = {1, 2, 3, 4, 5, 6};
        Flux<Integer> integerFlux = Flux.fromArray(array);
        integerFlux.toStream(3).forEach(System.out::print);
        List<Integer> integers = Arrays.asList(array);
        Flux.fromIterable(integers);
        Flux.fromStream(integers.stream());

        // empty
        Flux.empty();
        Mono.empty();
        Mono.justOrEmpty(Optional.empty());

        //error
        Flux.error(new Exception());
        Mono.error(new Exception());
    }

    @Test
    public void testSubscribe() {
        Flux.just(1, 2, 3, 4, 5, 6).subscribe(System.out::println,
                System.err::println,
                () -> System.out.println("completion"));
        Mono.just(100).subscribe(System.out::print);
    }

    @Test
    public void testSubscribeError() {
        Flux.error(new Exception("出错了！")).subscribe(System.out::println,
                System.err::println,
                () -> System.out.println("completion"));
        Mono.error(new Exception("mono error！")).subscribe(System.out::print, System.err::println);
    }

    //test
    @Test
    public void testViaStepVerifier() {
        StepVerifier.create(Flux.just(1, 2, 3, 4, 5, 6))
                .expectNext(1, 2, 3, 4, 5, 6)
                .expectComplete()
                .verify();
        StepVerifier.create(Mono.error(new Exception("some error")))
                .expectErrorMessage("some error")
                .verify();

        System.out.println("----------------withVirtualTime----------------");
        StepVerifier.withVirtualTime(() -> Flux.interval(Duration.ofHours(4), Duration.ofDays(1)).take(2))
                .expectSubscription()
                .expectNoEvent(Duration.ofHours(4))
                .expectNext(0L)
                .thenAwait(Duration.ofDays(1))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    public void flatMap() {
        StepVerifier.create(
                Flux.just("flux", "mono")
                        .flatMap(s -> Flux.fromArray(s.split("\\s*")))
                        .delayElements(Duration.ofMillis(100))
                        .doOnNext(System.out::println)
        )
                .expectNextCount(8)
                .verifyComplete();
    }

    private Flux<String> getZipDescFlux() {
        String desc = "Zip two sources together, that is to say wait for all the sources to emit one element and combine these elements once into a Tuple2.";
        return Flux.fromArray(desc.split("\\s+"));  // 1
    }

    @Test
    //zip能够一对一地将两个或多个数据流的元素对齐发出
    public void testZip() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        Flux.zip(getZipDescFlux(), Flux.interval(Duration.ofMillis(200)))
                .subscribe(System.out::println, null, countDownLatch::countDown);
        countDownLatch.await(5, TimeUnit.SECONDS);

        Flux.just("a", "b")
                .zipWith(Flux.just("c", "d"))
                .subscribe(System.out::println);

        Flux.just("a", "b")
                .zipWith(Flux.just("c", "d"),(a,b) ->String.format("%s-%s",a,b))
                .subscribe(System.out::println);
    }

    @Test
    public void interval() throws InterruptedException {
        Flux.interval(Duration.ofMillis(100)).subscribe(System.out::println, System.err::println);
        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    public void generate() {
        LongAdder longAdder = new LongAdder();
        Flux.generate(sink -> {
            sink.next(longAdder.longValue());
            longAdder.increment();
            if (longAdder.longValue() > 100) {
                sink.complete();
            }
        }).skip(50).take(10).subscribe(System.out::println, System.err::println);
    }

    @Test
    /**
     * 用于计数；
     * 向“池子”放自定义的数据；
     * 告诉generate方法，自定义数据已发完；
     * 触发数据流。
     */
    public void testGenerate1() {
        final AtomicInteger count = new AtomicInteger(1);   // 1
        Flux.generate(sink -> {
            sink.next(count.get() + " : " + new Date());   // 2
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (count.getAndIncrement() >= 5) {
                sink.complete();     // 3
            }
        }).subscribe(System.out::println);  // 4
    }

    @Test
    /**
     * 初始化状态值；
     * 第二个参数是BiFunction，输入为状态和sink；
     * 每次循环都要返回新的状态值给下次使用。
     */
    public void testGenerate2() {
        Flux.generate(
                () -> 1,    // 1
                (count, sink) -> {      // 2
                    sink.next(count + " : " + new Date());
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (count >= 5) {
                        sink.complete();
                    }
                    return count + 1;   // 3
                }).subscribe(System.out::println);
    }

    @Test
    //最后将count值打印出来。
    public void testGenerate3() {
        Flux.generate(
                () -> 1,    // 1
                (count, sink) -> {      // 2
                    sink.next(count + " : " + new Date());
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (count >= 5) {
                        sink.complete();
                    }
                    return count + 1;   // 3
                }, System.out::println).subscribe(System.out::println);
    }

    @Test
    public void create() {
        Flux.create(sink -> {
            for (int i = 0; i < 100; i++) {
                sink.next(i);
            }
            sink.complete();
        }).subscribe(System.out::println);
    }

    @Test
    public void buffer() {
        Flux.range(1, 100).buffer(20).subscribe(System.out::println);
        Flux.range(1, 100).buffer(20,5).subscribe(System.out::println);
        Flux.range(1, 10).bufferUntil(i -> i % 2 == 0).subscribe(System.out::println);
        Flux.range(1, 10).bufferWhile(i -> i % 2 == 0).subscribe(System.out::println);
    }

    @Test
    public void reduce() {
        //第一行语句对流中的元素进行相加操作，结果为 5050；第二行语句同样也是进行相加操作，不过通过一个 Supplier 给出了初始值为 100，所以结果为 5150。
        Flux.range(1, 100).reduce((x, y) -> x + y).subscribe(System.out::println);
        Flux.range(1, 100).reduceWith(() -> 100, (x, y) -> x + y).subscribe(System.out::println);
    }

    @Test
    public void merge() {
        /**
         * 在使用 merge 的结果流中，来自两个流的元素是按照时间顺序交织在一起；
         * 而使用 mergeSequential 的结果流则是首先产生第一个流中的全部元素，
         * 再产生第二个流中的全部元素。
         */
        Flux.merge(Flux.interval(Duration.ofMillis(100)).take(5), Flux.interval(Duration.ofMillis(50), Duration.ofMillis(100)).take(5))
                .toStream()
                .forEach(System.out::println);
        System.out.println("------------------------------------");
        Flux.mergeSequential(Flux.interval(Duration.ofMillis(100)).take(5), Flux.interval(Duration.ofMillis(50), Duration.ofMillis(100)).take(5))
                .toStream()
                .forEach(System.out::println);
    }

    @Test
    public void concatMap() {
        Flux.just(5,10).concatMap(x -> Flux.interval(Duration.ofMillis(100)).take(x))
                .toStream().forEach(System.out::println);
    }

    @Test
    public void combineLatest() {
        Flux.combineLatest(Arrays::toString,
                Flux.interval(Duration.ofMillis(100)).take(5),
                Flux.interval(Duration.ofMillis(50)).take(10))
        .toStream().forEach(System.out::println);
    }

    @Test
    public void schedulers() {
        Flux.create(sink -> {
            sink.next(Thread.currentThread().getName());
            sink.complete();
        }).publishOn(Schedulers.single())
                .map(x -> String.format("[%s]- %s",Thread.currentThread().getName(),x))
                .publishOn(Schedulers.elastic())
                .map(x -> String.format("[%s]- %s",Thread.currentThread().getName(),x))
                .subscribeOn(Schedulers.parallel())
                .toStream().forEach(System.out::println);
    }

    @Test
    /**
     * 通过 publish()方法把一个 Flux 对象转换成 ConnectableFlux 对象。
     * 方法 autoConnect()的作用是当 ConnectableFlux 对象有一个订阅者时就开始产生消息。
     * 代码 source.subscribe()的作用是订阅该 ConnectableFlux 对象，让其开始产生数据。
     * 接着当前线程睡眠 5 秒钟，第二个订阅者此时只能获得到该序列中的后 5 个元素，
     * 因此所输出的是数字 5 到 9。
     */
    public void testHot() throws InterruptedException {
        final Flux<Long> source = Flux.interval(Duration.ofMillis(100))
                .take(10)
                .publish()
                .autoConnect();
        Thread.sleep(500);
        source.subscribe();
        Thread.sleep(500);
        source.toStream().forEach(System.out::println);
    }
}
