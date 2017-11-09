package de.zalando.zmon.dataservice.data;

import de.zalando.zmon.dataservice.DataServiceMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SqsResultWriter implements WorkResultWriter{

    public static final String SQS_WRITER_EXECUTOR = "sqs-writer";
    private final Logger log = LoggerFactory.getLogger(RedisWorkerResultWriter.class);

    private final DataServiceMetrics metrics;

    @Autowired
    SqsResultWriter(DataServiceMetrics metrics)
    {
        this.metrics = metrics;
    }

    @Autowired
    protected JmsTemplate defaultJmsTemplate;

    @Async(SQS_WRITER_EXECUTOR)
    @Override
    public void write(WriteData writeData) {
        defaultJmsTemplate.convertAndSend("zmon-data-consumer",
                writeData.toString());
    }

}
