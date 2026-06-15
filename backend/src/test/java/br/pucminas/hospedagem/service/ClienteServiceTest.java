package br.pucminas.hospedagem.service;

import br.pucminas.hospedagem.model.Aluguel;
import br.pucminas.hospedagem.model.Cliente;
import br.pucminas.hospedagem.repository.AluguelRepository;
import br.pucminas.hospedagem.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private AluguelRepository aluguelRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void deveSalvarClienteNovo() {
        Cliente cliente = criarCliente(1L, "Carlos", "12345678900");

        when(clienteRepository.findByCpf(cliente.getCpf())).thenReturn(Optional.empty());
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente resultado = clienteService.salvar(cliente);

        assertNotNull(resultado);
        assertEquals("Carlos", resultado.getNome());
        assertEquals("12345678900", resultado.getCpf());

        verify(clienteRepository).save(cliente);
    }

    @Test
    void deveLancarExcecaoAoSalvarClienteComCpfDuplicado() {
        Cliente cliente = criarCliente(1L, "Carlos", "12345678900");

        when(clienteRepository.findByCpf(cliente.getCpf())).thenReturn(Optional.of(cliente));

        assertThrows(IllegalArgumentException.class, () -> clienteService.salvar(cliente));

        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveBuscarClientePorId() {
        Cliente cliente = criarCliente(1L, "Carlos", "12345678900");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Optional<Cliente> resultado = clienteService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Carlos", resultado.get().getNome());
    }

    @Test
    void deveBuscarClientePorCpf() {
        Cliente cliente = criarCliente(1L, "Carlos", "12345678900");

        when(clienteRepository.findByCpf("12345678900")).thenReturn(Optional.of(cliente));

        Optional<Cliente> resultado = clienteService.buscarPorCpf("12345678900");

        assertTrue(resultado.isPresent());
        assertEquals("12345678900", resultado.get().getCpf());
    }

    @Test
    void deveRetornarFalseAoRemoverClienteInexistente() {
        when(clienteRepository.existsById(99L)).thenReturn(false);

        boolean resultado = clienteService.remover(99L);

        assertFalse(resultado);
        verify(clienteRepository, never()).deleteById(anyLong());
    }

    @Test
    void deveListarHistoricoDoCliente() {
        Long clienteId = 1L;
        Aluguel aluguel = new Aluguel();

        when(clienteRepository.existsById(clienteId)).thenReturn(true);
        when(aluguelRepository.findByClienteId(clienteId)).thenReturn(List.of(aluguel));

        List<Aluguel> resultado = clienteService.listarHistorico(clienteId);

        assertEquals(1, resultado.size());
        verify(aluguelRepository).findByClienteId(clienteId);
    }

    @Test
    void deveLancarExcecaoAoListarHistoricoDeClienteInexistente() {
        Long clienteId = 99L;

        when(clienteRepository.existsById(clienteId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> clienteService.listarHistorico(clienteId));

        verify(aluguelRepository, never()).findByClienteId(anyLong());
    }

    private Cliente criarCliente(Long id, String nome, String cpf) {
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNome(nome);
        cliente.setCpf(cpf);
        return cliente;
    }
}