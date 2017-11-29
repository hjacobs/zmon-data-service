package de.zalando.zmon.dataservice.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TestPropertySource(properties = {
        "tracing.lightstep.access_token=foobarbaz",
        "tracing.lightstep.collector_host=iamthedomain",
        "tracing.lightstep.collector_port=8088",
        "tracing.lightstep.collector_protocol=http",
        "tracing.lightstep.component_name=data-foo",
})
public class LightstepConfigurationTest {

    @Autowired
    private LightstepConfiguration config;

    @Test
    public void testAccessTokenComesFromConfiguration() {
        assertThat(config.getAccessToken(), is("foobarbaz"));
        assertThat(config.getCollectorHost(), is("iamthedomain"));
        assertThat(config.getCollectorPort(), is(8088));
        assertThat(config.getCollectorProtocol(), is("http"));
        assertThat(config.getComponentName(), is("data-foo"));
    }

    @Configuration
    @EnableConfigurationProperties({LightstepConfiguration.class})
    static class Config {
    }

}
