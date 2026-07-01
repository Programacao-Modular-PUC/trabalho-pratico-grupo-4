package br.pucminas.hospedagem.controller;

import br.pucminas.hospedagem.tarifa.GerenciadorTarifas;
import br.pucminas.hospedagem.tarifa.PoliticaTarifa;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tarifas")
public class TarifaController {

    // O gerenciador e um Singleton: o controller consulta a instancia unica
    // em vez de manter uma referencia propria injetada.

    @GetMapping
    public Map<String, Object> listar() {
        GerenciadorTarifas gerenciador = GerenciadorTarifas.getInstance();

        List<Map<String, String>> politicas = new ArrayList<>();
        for (PoliticaTarifa politica : gerenciador.listarPoliticas()) {
            Map<String, String> item = new LinkedHashMap<>();
            item.put("nome", politica.getNome());
            item.put("descricao", politica.getDescricao());
            politicas.add(item);
        }

        Map<String, Object> resposta = new LinkedHashMap<>();
        resposta.put("politicaAtiva", gerenciador.getPoliticaAtiva().getNome());
        resposta.put("politicas", politicas);
        return resposta;
    }

    @PutMapping("/ativa")
    public Map<String, String> ativar(@RequestParam String politica) {
        GerenciadorTarifas gerenciador = GerenciadorTarifas.getInstance();
        gerenciador.ativarPolitica(politica);
        return Map.of("politicaAtiva", gerenciador.getPoliticaAtiva().getNome());
    }
}
