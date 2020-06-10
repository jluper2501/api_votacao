package br.com.votacao.votacao.controller;

import br.com.votacao.votacao.dto.PautasDTO;
import br.com.votacao.votacao.service.PautasService;
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
@RequestMapping(path = "/api/v1/pautas")
@Api(value = "Pautas", tags = "Pautas")
public class PautasController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PautasController.class);

    private final PautasService service;

    @Autowired
    public PautasController(PautasService service) {
        this.service = service;
    }

    @ApiOperation(value = "Criacao de pauta para ser votada")
    @PostMapping
    public ResponseEntity<PautasDTO> salvarPautas(@Valid @RequestBody PautasDTO dto) {
        LOGGER.debug("Salvando a pauta  = {}", dto.getDescricao());
        dto = service.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @ApiOperation(value = "Buscar a pauta utilizando ID")
    @GetMapping(value = "/{id}")
    public ResponseEntity<PautasDTO> buscarPautasPeloID(@PathVariable("id") Integer id) {
        LOGGER.debug("Buscando pautas pelo ID = {}", id);
        return ResponseEntity.ok(service.buscarPautasPeloOID(id));
    }
}
