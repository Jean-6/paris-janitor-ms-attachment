package com.example.parisjanitormsattachment.dto;


import lombok.AllArgsConstructor;


@AllArgsConstructor
public class ErrorResponse {
    private int errorCode;
    private String message;
    private long timestamp;
}
