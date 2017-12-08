package de.zalando.zmon.dataservice.config;

import java.util.concurrent.*;

import com.uber.jaeger.context.TracingUtils;
import de.zalando.zmon.dataservice.data.KairosDbWorkResultWriter;
import de.zalando.zmon.dataservice.data.RedisWorkerResultWriter;
import io.opentracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;

/**
 * Created by jmussler on 04.05.16.
 */
@Configuration
@EnableAsync
public class ZMonAsyncConfigurer extends AsyncConfigurerSupport {

    private final static Logger LOG = LoggerFactory.getLogger(ZMonAsyncConfigurer.class);

    private final DataServiceConfigProperties properties;
    private final MetricRegistry metricRegistry;

    @SuppressWarnings("unused") //  Forcing initialization order.
    private final Tracer tracer;

    public ZMonAsyncConfigurer(
            final DataServiceConfigProperties properties,
            final MetricRegistry metricRegistry,
            final Tracer tracer) {

        this.properties = properties;
        this.metricRegistry = metricRegistry;
        this.tracer = tracer;
    }

    @Override
    public Executor getAsyncExecutor() {
        return createExecutor("default");
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }

    @Bean(RedisWorkerResultWriter.REDIS_WRITER_EXECUTOR)
    public Executor redisWriterExecutor() {
        return createExecutor("redis");
    }

    @Bean(KairosDbWorkResultWriter.KAIROS_WRITER_EXECUTOR)
    public Executor kairosWriterExecutor() {
        return createExecutor("kairos");
    }

    private Executor createExecutor(final String name) {
        final AsyncExecutorProperties config = properties.getAsyncExecutors().getOrDefault(name, AsyncExecutorProperties.DEFAULT);
        final int queueSize = config.getQueueSize();
        final int coreSize = config.getCoreSize();
        final int maxSize = config.getMaxSize();

        LOG.info("Creating Async executor={} with core-size={} max-size={} queue-size={}", name, coreSize, maxSize, queueSize);
//        final ThreadPoolTaskExecutor executor = new CustomizableThreadPoolTaskExecutor(taskExecutorQueue(queueSize, name));
//        executor.setCorePoolSize(coreSize);
//        executor.setMaxPoolSize(maxSize);
//        executor.setQueueCapacity(queueSize); // will be ignored
//        executor.setThreadNamePrefix(String.format("zmon-async-%s-", name));
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
//        executor.initialize();

//        final ThreadPoolExecutorFactoryBean factory = new ThreadPoolExecutorFactoryBean();
//        factory.setCorePoolSize(coreSize);
//        factory.setMaxPoolSize(maxSize);
//        factory.setQueueCapacity(queueSize);
//        factory.setThreadNamePrefix(String.format("zmon-async-%s-", name));
//        factory.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());

//        ExecutorService executor = TracingUtils.tracedExecutor(factory.getObject());

//        return new ThreadPoolExecutor(corePoolSize, maxPoolSize,
//                keepAliveSeconds, TimeUnit.SECONDS, queue, threadFactory, rejectedExecutionHandler);
//
        LinkedBlockingQueue<Runnable> queue = taskExecutorQueue(queueSize, name);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                coreSize, maxSize, 60, TimeUnit.SECONDS, queue, new ThreadPoolExecutor.DiscardOldestPolicy());
        //return TracingUtils.tracedExecutor(executor);
        return executor;
    }

    private LinkedBlockingQueue<Runnable> taskExecutorQueue(final int capacity, final String name) {
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(capacity);
        metricRegistry.register(MetricRegistry.name("data-service.async.executor." + name, "queue", "size"), new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                return queue.size();
            }
        });
        return queue;
    }

    static class CustomizableThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

        private static final long serialVersionUID = 1L;

        private final LinkedBlockingQueue<Runnable> queue;

        CustomizableThreadPoolTaskExecutor(LinkedBlockingQueue<Runnable> queue) {
            this.queue = queue;
        }

        @Override
        protected BlockingQueue<Runnable> createQueue(int queueCapacity) {
            if (queue == null) {
                return super.createQueue(queueCapacity);
            } else {
                return queue;
            }
        }
    }
}
