package com.gabriel.gerenciamentoloja.exception;

public class ClienteJaCadastradoException extends RuntimeException {
    public ClienteJaCadastradoException(String message, Throwable causa) {
        super(message, causa);
    }
}
