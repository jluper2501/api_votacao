package br.com.votacao.votacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VotacaoNotFoundException extends RuntimeException {

    public VotacaoNotFoundException(String mensagem) {
            super(mensagem);
    }
}
