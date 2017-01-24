package com.github.krelix.main;

import com.github.krelix.storage.StorageService;
import com.github.krelix.utils.FileCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;

import java.io.File;

/**
 * Created by brizarda on 20/01/2017.
 */
@SpringBootApplication
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
        // TODO : Implement a fixed amount of file to generate and inject
        return args -> {
            StorageService service = new StorageService(restTemplate);
            //File fileToUpload = FileCreator.generateRandomFile(4*TO_ABOVE_BYTE_RANGE);
            Flux<File> files = FileCreator.randomFilesCreator(100);
            StopWatch watch = new StopWatch();
            watch.start();
            files.subscribe( (f) -> LOGGER.info("Obtained file {} of size {}", f.getName(), f.length()));
            watch.stop();
            LOGGER.info("FINISHED GENERATING FILES IN {} MS", watch.getLastTaskTimeMillis());
        };

    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
