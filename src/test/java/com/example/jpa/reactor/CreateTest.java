package com.example.jpa.reactor;

import org.junit.Test;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class CreateTest {

    @Test
    /**
     * 事件源；
     * 向事件源注册用匿名内部类创建的监听器；
     * 监听器在收到事件回调的时候通过sink将事件再发出；
     * 监听器再收到事件源停止的回调的时候通过sink发出完成信号；
     * 触发订阅（这时候还没有任何事件产生）；
     * 循环产生20个事件，每个间隔不超过1秒的随机时间；
     * 最后停止事件源。
     */
    public void testCreate() throws InterruptedException {
        MyEventSource eventSource = new MyEventSource();    // 1
        Flux.create(sink -> eventSource.register(new MyEventListener() {    // 2
            @Override
            public void onNewEvent(MyEventSource.MyEvent event) {
                sink.next(event);       // 3
            }

            @Override
            public void onEventStopped() {
                sink.complete();        // 4
            }
        })
        ).subscribe(System.out::println);       // 5

        for (int i = 0; i < 20; i++) {  // 6
            Random random = new Random();
            TimeUnit.MILLISECONDS.sleep(random.nextInt(300));
            eventSource.newEvent(new MyEventSource.MyEvent(LocalDateTime.now(), "Event-" + i));
        }
        eventSource.eventStopped(); // 7
    }

    @Test
    public void testTransform() {
        Function<Flux<String>, Flux<String>> filterAndMap =
                f -> f.filter(color -> !color.equals("orange"))
                        .map(String::toUpperCase);

        Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
                .doOnNext(System.out::println)
                .transform(filterAndMap)
                .subscribe(d -> System.out.println("Subscriber to Transformed MapAndFilter: "+d));
    }

    @Test
    public void testCompose() {
        AtomicInteger ai = new AtomicInteger();
        Function<Flux<String>, Flux<String>> filterAndMap = f -> {
            if (ai.incrementAndGet() == 1) {
                return f.filter(color -> !color.equals("orange"))
                        .map(String::toUpperCase);
            }
            return f.filter(color -> !color.equals("purple"))
                    .map(String::toUpperCase);
        };

        Flux<String> composedFlux =
                Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
                        .doOnNext(System.out::println)
                        .compose(filterAndMap);

        composedFlux.subscribe(d -> System.out.println("Subscriber 1 to Composed MapAndFilter :" + d));
        composedFlux.subscribe(d -> System.out.println("Subscriber 2 to Composed MapAndFilter: " + d));
    }

}
