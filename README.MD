消息队列
=============================
### 基于redis实现的消息队列

#### 依赖
```xml
<dependency>
    <groupId>com.ipet</groupId>
    <artifactId>quick-queue-redis</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
#### 环境
```properties
使用spring redis配置
spring.redis.password=wIvJt@_redis
spring.redis.database=1
spring.redis.sentinel.master=sharddev6389
spring.redis.sentinel.nodes=192.168.11.29:26002,192.168.11.32:26002,192.168.11.20:26002
#redis IO 10 ms 超时
spring.redis.timeout=1000
spring.redis.pool.max-active=-1
```
#### 调用
##### 生产者
```java
    //通过RedisQueueScanner注入消息队列接口
    @RedisQueueScanner("com.ipet.test.queue")
    @Configuration
    @SpringBootApplication
    public class ServiceExecutor {
    
        public static void main(String[] args) throws InterruptedException {
            new SpringApplication(ServiceExecutor.class).run(args);
            new CountDownLatch(1).await();
        }
    }
    
    //通过RedisQueue接口定义消息队列生产者interface
    //通过RedisQueueProvider接口定义生产者队列名称
    @RedisQueue
    public interface TestQueue {
    
        @RedisQueueProvider("queue1")
        void sendMessage1(String msg);
    
        @RedisQueueProvider("queue2")
        void sendMessage2(List<String> msgList);
    }
```
##### 消费者
```java
    //继承AbstractRedisQueueConsumer并通过RedisQueueConsumer注解标明队列名称即可
    @RedisQueueConsumer("queue1")
    public class Message1Consumer extends AbstractRedisQueueConsumer<String> {
        private Logger logger = LoggerFactory.getLogger(getClass());
        @Override
        public void doConsume(String message) {
            logger.info("Message1 : [{}]",message);
        }
    }
```