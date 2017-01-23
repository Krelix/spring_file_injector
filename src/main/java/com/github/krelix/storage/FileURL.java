package com.github.krelix.storage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by brizarda on 21/01/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileURL {
    private String fileUrl = "";

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public String toString() {
        return "FileURL{fileUrl : "+ this.fileUrl +"}";
    }
}
