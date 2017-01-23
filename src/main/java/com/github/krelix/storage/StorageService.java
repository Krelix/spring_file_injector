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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by brizarda on 21/01/2017.
 */
public class StorageService {
    private static final String STORAGE_URL = "http://127.0.0.1:8080/upload";
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageService.class);

    private RestTemplate template;

    public StorageService(RestTemplate template) {
        this.template = template;
    }

    public void uploadFile(File fileToUpload)
        throws IOException {

        HttpHeaders mainHeader = new HttpHeaders();
        mainHeader.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpHeaders fileHeaders = new HttpHeaders();
        fileHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        HttpEntity<FileSystemResource> fileEntity = new HttpEntity<>(new FileSystemResource(fileToUpload), fileHeaders);

        MultiValueMap<String, Object> multiPartRequest = new LinkedMultiValueMap<String, Object>();
        multiPartRequest.add("file", fileEntity);

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(multiPartRequest, mainHeader);

        LOGGER.info("POST content : {}",
                template.postForEntity(STORAGE_URL, entity, FileURL.class).toString());
    }
}
