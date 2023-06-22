package com.example.meusgastos.domain.exception;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String mensagem) {
        // manda a mensagem pro construtor da classe dela
        super(mensagem);
    }
}
