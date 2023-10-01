package solutions.tal.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;

@SpringBootApplication
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        new SpringApplicationBuilder(Main.class).web(WebApplicationType.NONE).run(args);
    }

    @Bean
    public IntegrationFlow loggingFlow() {
        return IntegrationFlow
                .from(ActivityProcessingConfiguration.ACTIVITY_FILE_RECORD_CHANNEL_NAME)
                .handle(m -> LOGGER.info("{} : {}", m.getHeaders(), m.getPayload()))
                .get();
    }
}
