package br.pucminas.hospedagem.repository;

import br.pucminas.hospedagem.model.Aluguel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AluguelRepository extends JpaRepository<Aluguel, Long> {

    List<Aluguel> findByQuartoId(Long quartoId);

    List<Aluguel> findByResidenciaId(Long residenciaId);

    List<Aluguel> findByClienteId(Long clienteId);

    @Query("SELECT a FROM Aluguel a WHERE a.quarto.id = :quartoId " +
            "AND a.dataEntrada < :saida AND a.dataSaida > :entrada")
    List<Aluguel> findConflitos(@Param("quartoId") Long quartoId,
                                @Param("entrada") LocalDateTime entrada,
                                @Param("saida") LocalDateTime saida);
}
