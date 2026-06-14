package br.pucminas.hospedagem.exception;

public class RecursoNaoPermitidoException extends RuntimeException {

    public RecursoNaoPermitidoException(String mensagem) {
        super(mensagem);
    }
}
