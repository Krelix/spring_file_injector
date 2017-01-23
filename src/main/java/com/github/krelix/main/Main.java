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
import org.springframework.web.client.RestTemplate;

import java.io.File;

/**
 * Created by brizarda on 20/01/2017.
 */
@SpringBootApplication
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final long TO_ABOVE_BYTE_RANGE = 1024L;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
        // TODO : Implement a fixed amount of file to generate and inject
        return args -> {
            StorageService service = new StorageService(restTemplate);
            File fileToUpload = FileCreator.generateRandomFile(4*TO_ABOVE_BYTE_RANGE);
            LOGGER.debug("File : {}", fileToUpload.toString());
            LOGGER.debug("File size : {}", fileToUpload.length());
            service.uploadFile(fileToUpload);
        };

    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
