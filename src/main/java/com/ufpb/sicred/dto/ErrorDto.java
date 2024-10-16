package com.ufpb.sicred.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorDto {
    private LocalDateTime timestamp;
    private Integer status;
    private List<String > errors;
    private String path;

    public ErrorDto(LocalDateTime timestamp, HttpStatus status, List<String> errors, String path) {
        this.timestamp = timestamp;
        this.status = status.value();
        this.errors = errors;
        this.path = path;
    }

    public ErrorDto() {
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
