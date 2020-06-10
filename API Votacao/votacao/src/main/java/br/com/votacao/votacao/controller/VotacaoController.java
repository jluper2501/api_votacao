package br.com.votacao.votacao.controller;

import br.com.votacao.votacao.dto.ResultadoDTO;
import br.com.votacao.votacao.dto.VotoDTO;
import br.com.votacao.votacao.service.VotacaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/votacao")
@Api(value = "Votacao", tags = "Votacao")

public class VotacaoController {
    private static final Logger LOGGER = LoggerFactory.getLogger(VotacaoController.class);

    private final VotacaoService service;

    @Autowired
    public VotacaoController(VotacaoService service) {
        this.service = service;
    }

    @ApiOperation(value = "Votar em determinada pauta, enquanto a sessão estiver aberta")
    @PostMapping(value = "/votar")
    public ResponseEntity<String> votar(@Valid @RequestBody VotoDTO dto) {
        LOGGER.debug("Associado votando - associado: {}", dto.getCpfAssociado());
        String mensagem = service.votar(dto);
        LOGGER.debug("Voto associado finalizado - associado: {}", dto.getCpfAssociado());
        return ResponseEntity.status(HttpStatus.CREATED).body(mensagem);
    }

    @ApiOperation(value = "Resultado da votacao, somente após a sessão estiver encerrada")
    @GetMapping(value = "/resultado/{idPauta}/{idSessaoVotacao}")
    public ResponseEntity<ResultadoDTO> resultadoVotacao(@PathVariable("idPauta") Integer idPauta, @PathVariable("idSessaoVotacao") Integer idSessaoVotacao) {
        LOGGER.debug("Buscando resultado da votacao idPauta = {} , idSessaoVotacao = {} ", idPauta, idSessaoVotacao);
        ResultadoDTO dto = service.buscarResultadoVotacao(idPauta, idSessaoVotacao);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
}
