package com.test.Schedule;

import com.test.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
public class ScheduleTest {

    @Value("${server.port}")
    private String port;
    @Autowired
    private RedisUtils redisUtils;

    //    @Scheduled(cron = "0 39 11 * * ?")     //固定时间
    @Async    // 加了此注解定时任务才能异步执行
    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24)   //每隔24小时执行，应用启动后立即执行
    public void testRedisLock1() throws InterruptedException {
        System.out.println("1111进来了"+port+Thread.currentThread().getName());
//        boolean lock = redisUtils.lock("redislock", "redislock");
        boolean lock = false;
        System.out.println("1111"+lock+" "+port);
        if(!lock){
            System.out.println("testRedisLock1执行了。。。"+port);
            Thread.sleep(3000);
//            boolean unlock = redisUtils.unlock("redislock", "redislock");
            System.out.println("111删除了"+port+" ");
        }
    }

    //    @Scheduled(cron = "0 39 11 * * ?")
    @Async
    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
    public void testRedisLock2() throws InterruptedException {
        System.out.println("2222进来了"+Thread.currentThread().getName());
//        boolean lock = redisUtils.lock("redislock", "redislock");
        boolean lock = false;
        System.out.println("2222"+lock);
        if(!lock){
            System.out.println("testRedisLock2执行了。。。");
            Thread.sleep(3000);
//            boolean unlock = redisUtils.unlock("redislock", "redislock");
            System.out.println("222删除了"+port+" ");
        }
    }
}
