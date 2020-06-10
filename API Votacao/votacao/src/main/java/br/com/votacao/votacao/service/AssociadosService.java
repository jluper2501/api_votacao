package br.com.votacao.votacao.service;

import br.com.votacao.votacao.controller.ValidaCPFAssociado;
import br.com.votacao.votacao.dto.AssociadoDTO;
import br.com.votacao.votacao.AssociadosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AssociadosService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssociadosService.class);
    private final AssociadosRepository repository;
    private ValidaCPFAssociado validaCPFAssociado;

    @Autowired
    public AssociadosService(AssociadosRepository repository, ValidaCPFAssociado validaCPFAssociado) {
        this.repository = repository;
        this.validaCPFAssociado = validaCPFAssociado;
    }

    /**
     * Valida se o associado jah votou na pauta informada pelo seu ID.
     *
     * Se nao encontrar registro, seu voto eh considerado valido e computado
     *
     * @param cpfAssociado @{@link br.com.votacao.votacao.entity.Associados} CPF Valido
     * @param idPauta     @{@link br.com.votacao.votacao.entity.Pautas} ID
     * @return - boolean
     */
    @Transactional(readOnly = true)
    public boolean isValidaParticipacaoAssociadoVotacao(String cpfAssociado, Integer idPauta) {
        LOGGER.debug("Validando participacao do associado na votacao da pauta - id: {}", idPauta);
        if (repository.existsByCpfAssociadoAndIdPauta(cpfAssociado, idPauta)) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * @param dto @{@link AssociadoDTO}
     */
    @Transactional
    public void salvarAssociado(AssociadoDTO dto) {
        LOGGER.debug("Registrando participacao do associado na votacao idAssociado = {}, idPauta = {}", dto.getCpfAssociado(), dto.getIdPauta());
        repository.save(AssociadoDTO.toEntity(dto));
    }

    /**
     * metodo que realiza a consulta em API externa
     * para validar por meio de um cpf valido, se o associado esta habilitado para votar
     *
     * @param cpf - @{@link AssociadoDTO} CPF valido
     * @return - boolean
     */
    public boolean isAssociadoPodeVotar(String cpf) {
        return validaCPFAssociado.isVerificaAssociadoHabilitadoVotacao(cpf);
    }

}
