package de.zalando.zmon.dataservice.config;

import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("jaeger")
public class TracingConfiguration {

    private String samplerType;
    private int samplerParam;
    private String serviceName;

    @Bean
    public Tracer tracer() {

        if (!GlobalTracer.isRegistered()) {
            com.uber.jaeger.Configuration.SamplerConfiguration samplerConfiguration
                    = new com.uber.jaeger.Configuration.SamplerConfiguration(samplerType, samplerParam);

            com.uber.jaeger.Configuration.ReporterConfiguration reporterConfiguration
                    = com.uber.jaeger.Configuration.ReporterConfiguration.fromEnv();

            com.uber.jaeger.Configuration configuration
                    = new com.uber.jaeger.Configuration(serviceName, samplerConfiguration, reporterConfiguration);

            Tracer tracer = configuration.getTracer();
            GlobalTracer.register(tracer);
        }

        return GlobalTracer.get();
    }

    public String getSamplerType() {
        return samplerType;
    }

    public void setSamplerType(String samplerType) {
        this.samplerType = samplerType;
    }

    public int getSamplerParam() {
        return samplerParam;
    }

    public void setSamplerParam(int samplerParam) {
        this.samplerParam = samplerParam;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}