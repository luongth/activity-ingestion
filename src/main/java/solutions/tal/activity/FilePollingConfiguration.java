package solutions.tal.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.dsl.Files;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Configuration
public class FilePollingConfiguration {

    public static final String FILE_SPLIT_RESULT_CHANNEL_NAME = "fileSplitResultChannel";

    private static final Logger LOGGER = LoggerFactory.getLogger(FilePollingConfiguration.class);

    @Bean
    public File inboundDirectory(final @Value("${inbound.staging.path}") String path) {
        return createDirectory(path);
    }

    @Bean
    public File inboundProcessedDirectory(final @Value("${inbound.processed.path}") String path) {
        return createDirectory(path);
    }

    @Bean
    public File inboundFailedDirectory(final @Value("${inbound.failed.path}") String path) {
        return createDirectory(path);
    }

    @Bean
    public TaskExecutor filePollingTaskExecutor(@Value("${inbound.file.poller.thread.pool.size:1}") int poolSize) {
        final var taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(poolSize);
        return taskExecutor;
    }

    @Bean
    public IntegrationFlow inboundFileSplitFlow(final File inboundDirectory,
                                                final @Value("${inbound.file.poll.delay.seconds:60}") long period,
                                                final TaskExecutor filePollingTaskExecutor,
                                                final @Value("${inbound.filename.regex}") String regex) {
        return IntegrationFlow
                .from(
                        Files.inboundAdapter(inboundDirectory).regexFilter(regex),
                        e -> e.poller(Pollers.fixedDelay(Duration.ofSeconds(period)).taskExecutor(filePollingTaskExecutor)))
                .split(
                        Files.splitter()
                                .markers()
                                .charset(StandardCharsets.ISO_8859_1)
                                .applySequence(true))
                .channel(c -> c.queue(FILE_SPLIT_RESULT_CHANNEL_NAME))
                .get();
    }

    private File createDirectory(String path) {
        var directory = new File(path);
        if (directory.mkdirs()) {
            LOGGER.debug("Created directory {}", path);
        }
        return directory;
    }

}
