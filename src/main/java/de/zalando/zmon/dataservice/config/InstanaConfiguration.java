package de.zalando.zmon.dataservice.config;

import com.instana.opentracing.InstanaTracer;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import io.opentracing.util.ThreadLocalActiveSpanSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InstanaConfiguration {
    @Bean
    public Tracer instanaTracer() {
        Tracer tracer = new InstanaTracer(new ThreadLocalActiveSpanSource());
        GlobalTracer.register(tracer);
        return tracer;
    }
}
