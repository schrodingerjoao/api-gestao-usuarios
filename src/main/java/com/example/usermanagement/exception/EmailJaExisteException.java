package com.example.usermanagement.exception;

public class EmailJaExisteException extends RuntimeException {
    public EmailJaExisteException(String mensagem) {
        super(mensagem);
    }
}
