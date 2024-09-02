package com.spring.producer.dto;

import lombok.Data;

@Data
public class ApiResponseDTO<T> {
    private int status;
    private String message;
    private T data;


    public ApiResponseDTO(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
