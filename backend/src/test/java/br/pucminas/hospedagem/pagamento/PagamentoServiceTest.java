package br.pucminas.hospedagem.pagamento;

import br.pucminas.hospedagem.model.Aluguel;
import br.pucminas.hospedagem.model.FormaPagamento;
import br.pucminas.hospedagem.model.Pagamento;
import br.pucminas.hospedagem.model.StatusPagamento;
import br.pucminas.hospedagem.repository.AluguelRepository;
import br.pucminas.hospedagem.repository.PagamentoRepository;
import br.pucminas.hospedagem.service.PagamentoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PagamentoServiceTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private AluguelRepository aluguelRepository;

    @InjectMocks
    private PagamentoService pagamentoService;

    @Test
    @DisplayName("Pagamento de aluguel existente e confirmado")
    void confirmaPagamentoDeAluguelExistente() {
        Aluguel aluguel = new Aluguel();
        aluguel.setId(1L);
        aluguel.setValorFinal(250.0);
        when(aluguelRepository.findById(1L)).thenReturn(Optional.of(aluguel));
        when(pagamentoRepository.save(any(Pagamento.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));

        Pagamento pagamento = pagamentoService.criar(1L, FormaPagamento.PIX);

        assertEquals(250.0, pagamento.getValor());
        assertEquals(FormaPagamento.PIX, pagamento.getFormaPagamento());
        assertEquals(StatusPagamento.CONFIRMADO, pagamento.getStatus());
        assertSame(aluguel, pagamento.getAluguel());
        verify(pagamentoRepository).save(pagamento);
    }

    @Test
    @DisplayName("Pagamento rejeita aluguel inexistente")
    void rejeitaAluguelInexistente() {
        when(aluguelRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> pagamentoService.criar(99L, FormaPagamento.PIX));
        verify(pagamentoRepository, never()).save(any(Pagamento.class));
    }
}
