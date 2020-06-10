package br.com.votacao.votacao.service;

import br.com.votacao.votacao.dto.PautasDTO;
import br.com.votacao.votacao.entity.Pautas;
import br.com.votacao.votacao.exception.VotacaoNotFoundException;
import br.com.votacao.votacao.repository.PautasRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PautasService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PautasService.class);

    private final PautasRepository repository;

    @Autowired
    public PautasService(PautasRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public PautasDTO salvar(PautasDTO dto) {
        return PautasDTO.toDTO(repository.save(PautasDTO.toEntity(dto)));
    }


    /**
     * Realiza a busca da pauta pelo ID.
     * Se nao encontrar retorna httpStatus 404 direto para o client da API.
     * Se encontrar faz a conversao para DTO
     *
     * @param id - @{@link Pautas} ID
     * @return - @{@link PautasDTO}
     */
    @Transactional(readOnly = true)
    public PautasDTO buscarPautaPeloID(Integer id) {
        Optional<Pautas> pautaOptional = repository.findById(id);

        if (!pautaOptional.isPresent()) {
            LOGGER.error("Pauta não localizada para id {}", id);
            throw new NotFoundException("Pauta não localizada para o id " + id);
        }

        return PautasDTO.toDTO(pautaOptional.get());
    }

    /**
     * Valida a existencia da pauta na base de dados.
     * Se existir  é considerada valida para votacao.
     *
     * @param id - @{@link Pautas} ID
     * @return - boolean
     */
    @Transactional(readOnly = true)
    public boolean isPautaValida(Integer id) {
        if (repository.existsById(id)) {
            return Boolean.TRUE;
        } else {
            LOGGER.error("Pauta não localizada para id {}", id);
            throw new NotFoundException("Pauta não localizada para o id " + id);
        }
    }
}


