package com.github.krelix.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by brizarda on 21/01/2017.
 */
public class StorageService {
    private static final String STORAGE_URL = "http://127.0.0.1:8080/upload";
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageService.class);

    public void uploadFile(File fileToUpload, RestTemplate restTemplate) {
        HttpHeaders mainHeader = new HttpHeaders();
        mainHeader.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpHeaders fileHeaders = new HttpHeaders();
        fileHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        HttpEntity<FileSystemResource> fileEntity = new HttpEntity<>(new FileSystemResource(fileToUpload), fileHeaders);

        MultiValueMap<String, Object> multiPartRequest = new LinkedMultiValueMap<>();
        multiPartRequest.add("file", fileEntity);

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(multiPartRequest, mainHeader);

        LOGGER.info("Obtained result from rest call : {}",
                restTemplate.postForEntity(STORAGE_URL, entity, FileURL.class).toString());
    }

    // TODO : async upload to separate them in multiple threads ?
    public void uploadFiles(Flux<File> files, RestTemplate restTemplate) {
        final Scheduler myScheduler = Schedulers.newParallel(4, Executors.defaultThreadFactory());
        CountDownLatch latch = new CountDownLatch(200);
        files.subscribeOn(myScheduler)
                .parallel()
                .doAfterTerminate(myScheduler::dispose)
                .subscribe((f) -> {
                    RestTemplate myRestTemplate = new RestTemplate();
                    HttpHeaders mainHeader = new HttpHeaders();
                    mainHeader.setContentType(MediaType.MULTIPART_FORM_DATA);


                    HttpHeaders fileHeaders = new HttpHeaders();
                    fileHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);

                    HttpEntity<FileSystemResource> fileEntity = new HttpEntity<>(new FileSystemResource(f), fileHeaders);

                    MultiValueMap<String, Object> multiPartRequest = new LinkedMultiValueMap<>();
                    multiPartRequest.add("file", fileEntity);

                    HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(multiPartRequest, mainHeader);

                    LOGGER.info("Obtained result from rest call : {}",
                            myRestTemplate.postForEntity(STORAGE_URL, entity, FileURL.class).toString());
                    latch.countDown();
                });
    }
}
