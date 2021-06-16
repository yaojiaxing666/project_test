package com.test.controller;

import com.test.util.SpringUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/thread")
@Log
public class ThreadController {
    @Autowired
    private TestRedisController testRedisController;

    @GetMapping("/thread")
    public void thread(){
        System.out.println(Thread.currentThread().getName()+"111");
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName()+"222");
            }
        });
        // 本类@Async 标注的方法不会异步执行
        async("");
        // 本类@Async 标注的方法 需要通过spring容器获取示例再进行调用，只有这样方法才能被代理。
        ThreadController bean = SpringUtil.getBean(ThreadController.class);
        bean.async(" new");
        // 只有在其他类中的@Async 标注的方法才会异步执行
        testRedisController.redisAsync();
        System.out.println(Thread.currentThread().getName()+"333");

    }

    @Async
    public void async(String s){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+"555 "+s);
    }

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CountDownLatch countDownLatch = new CountDownLatch(3);
        log.info("====lail==========");
        for (int i = 0; i < 3; i++) {
            int index=i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    test(index,countDownLatch);
                }
            });
        }
        try {
//            countDownLatch.await(15, TimeUnit.SECONDS);
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        log.info("====end==========");

    }

    public static void test(int index,CountDownLatch countDownLatch){
        log.info("ThreadName=========:"+Thread.currentThread().getName());
        try {
            Thread.sleep(5000*index);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("index=========:"+index);
        countDownLatch.countDown();
    }

}
