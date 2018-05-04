package com.example.jpa.reactor;

import org.junit.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 相对于传统的基于回调和Future的异步开发方式，响应式编程更加具有可编排性和可读性，配合lambda表达式，
 * 代码更加简洁，处理逻辑的表达就像装配“流水线”，适用于对数据流的处理；
 * 在订阅（subscribe）时才触发数据流，这种数据流叫做“冷”数据流，
 * 就像插座插上电器才会有电流一样，还有一种数据流不管是否有订阅者订阅它都会一直发出数据，
 * 称之为“热”数据流，Reactor中几乎都是“冷”数据流；
 * 调度器对线程管理进行更高层次的抽象，使得我们可以非常容易地切换线程执行环境；
 * 灵活的错误处理机制有利于编写健壮的程序；
 * “回压”机制使得订阅者可以无限接受数据并让它的源头“满负荷”推送所有的数据，
 * 也可以通过使用request方法来告知源头它一次最多能够处理 n 个元素，
 * 从而将“推送”模式转换为“推送+拉取”混合的模式。
 */
public class SchedulerTest {

    /**
     * Reactor 提供了两种在响应式链中调整调度器 Scheduler的方法：publishOn和subscribeOn。
     * 它们都接受一个 Scheduler作为参数，从而可以改变调度器。
     * 但是publishOn在链中出现的位置是有讲究的，而subscribeOn 则无所谓。
     */
    private String getStringSync() throws InterruptedException {
        Thread.sleep(200);
        return "hello!";
    }

    @Test
    public void testSyncToAsync() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Mono.fromCallable(() -> getStringSync())    // 1
                .subscribeOn(Schedulers.elastic())  // 2
                .subscribe(System.out::println, null, countDownLatch::countDown);
        System.out.println("---------------no block--------------");
        countDownLatch.await(1, TimeUnit.SECONDS);
    }

    @Test
    public void test1() {
        Flux.range(1, 1000)
                .map(i -> i^2)
                .publishOn(Schedulers.elastic()).filter(i -> (i & 1) ==0)
//                .publishOn(Schedulers.parallel()).map(i -> i+1)
                .subscribeOn(Schedulers.single())
                .toStream().forEach(System.out::println);
    }

    @Test
    public void testError() {
        Flux.range(6,10)
                .map(i -> i/(10-i))
                // 记录日志不影响流程
                .doOnError(System.out::println)
                // 重试是从头开始的
                .retry(1)
//                .onErrorReturn(100)
                .onErrorResume(e -> Mono.just(new Random().nextInt(6)))
                .map(i -> i^2)
                .subscribe(System.out::println, System.err::println);
    }

    @Test
    public void testErrorMap() {
        Flux.range(6,10)
                .map(i -> i/(10-i))
                // 重新抛出
                .onErrorMap(e -> new Exception("error!"))
                .map(i -> i^2)
                .subscribe(System.out::println, System.err::println);
    }

    @Test
    // 背压
    public void testBackpressure() {
        Flux.range(1,6)
                .doOnRequest(i -> System.out.printf("request:%d \n",i))
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        System.out.println("hookOnSubscribe 订阅的时候请求来一个");
                        super.request(1);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.printf("hookOnNext: %d \n",value);
                        super.request(1);
                    }
                });
    }
}
