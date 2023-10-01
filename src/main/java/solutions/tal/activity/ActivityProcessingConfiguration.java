package solutions.tal.activity;

import com.ancientprogramming.fixedformat4j.format.impl.FixedFormatManagerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.file.splitter.FileSplitter;
import solutions.tal.activity.model.ActivityFileRecord;
import solutions.tal.activity.service.ActivityService;
import solutions.tal.activity.service.DefaultActivityService;

@Configuration
public class ActivityProcessingConfiguration {

    public static final String ACTIVITY_FILE_RECORD_CHANNEL_NAME = "activityFileRecordChannel";

    public static final String ACTIVITY_CHANNEL_NAME = "activityChannel";

    @Bean
    public Converter<String, ActivityFileRecord> fixedWidthActivityConverter() {
        final var manager = new FixedFormatManagerImpl();
        return source -> manager.load(ActivityFileRecord.class, source);
    }

    @Bean
    public IntegrationFlow transformFlow(final IntegrationFlow inboundFileSplitFlow, final Converter<String, ActivityFileRecord> fixedWidthActivityConverter) {
        return IntegrationFlow
                .from(inboundFileSplitFlow)
                .filter((payload) -> !(payload instanceof FileSplitter.FileMarker))
                .transform(Transformers.converter(fixedWidthActivityConverter))
                .channel(ACTIVITY_FILE_RECORD_CHANNEL_NAME)
                .get();
    }

    @Bean
    public ActivityService activityService() {
        return new DefaultActivityService();
    }

    @Bean
    public IntegrationFlow enrichFlow(final IntegrationFlow transformFlow, final ActivityService activityService) {
        return IntegrationFlow
                .from(transformFlow)
                .handle(activityService, "process")
                .channel(ACTIVITY_CHANNEL_NAME)
                .get();
    }
}
