package br.com.votacao.votacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.LOCKED)
public class SessaoFechadaException extends RuntimeException {

    public SessaoFechadaException(String mensagem) {
        super(mensagem);
    }
}
