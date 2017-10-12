package de.zalando.zmon.dataservice.config;

import com.instana.opentracing.InstanaTracer;
import io.opentracing.Tracer;
import io.opentracing.util.ThreadLocalActiveSpanSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InstanaConfiguration {
    @Bean
    public Tracer instanaTracer() {
        return new InstanaTracer(new ThreadLocalActiveSpanSource());
    }
}
