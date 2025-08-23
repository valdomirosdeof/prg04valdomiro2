package br.com.ifba.infrastructure.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BusinessExceptionDetails {
    private LocalDateTime timestamp;
    private int status;
    private String title;
    private String details;
    private String developerMessage;
}