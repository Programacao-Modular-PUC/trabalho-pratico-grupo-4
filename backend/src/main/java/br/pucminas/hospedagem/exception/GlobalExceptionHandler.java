package br.pucminas.hospedagem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(QuartoIndisponivelException.class)
    public ResponseEntity<Map<String, Object>> tratarQuartoIndisponivel(QuartoIndisponivelException ex) {
        return montarResposta(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(CapacidadeExcedidaException.class)
    public ResponseEntity<Map<String, Object>> tratarCapacidadeExcedida(CapacidadeExcedidaException ex) {
        return montarResposta(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler(DataInvalidaException.class)
    public ResponseEntity<Map<String, Object>> tratarDataInvalida(DataInvalidaException ex) {
        return montarResposta(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(RecursoNaoPermitidoException.class)
    public ResponseEntity<Map<String, Object>> tratarRecursoNaoPermitido(RecursoNaoPermitidoException ex) {
        return montarResposta(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> tratarArgumentoInvalido(IllegalArgumentException ex) {
        return montarResposta(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> tratarEstadoInvalido(IllegalStateException ex) {
        return montarResposta(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> tratarErroGenerico(Exception ex) {
        return montarResposta(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno: " + ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> montarResposta(HttpStatus status, String mensagem) {
        Map<String, Object> corpo = new LinkedHashMap<>();
        corpo.put("timestamp", LocalDateTime.now());
        corpo.put("status", status.value());
        corpo.put("erro", status.getReasonPhrase());
        corpo.put("mensagem", mensagem);
        return ResponseEntity.status(status).body(corpo);
    }
}
