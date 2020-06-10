package br.com.votacao.votacao.service;

import br.com.votacao.votacao.dto.AbrirSessaoVotacaoDTO;
import br.com.votacao.votacao.dto.SessaoVotacaoDTO;
import br.com.votacao.votacao.entity.SessaoVotacao;
import br.com.votacao.votacao.exception.VotacaoNotFoundException;
import br.com.votacao.votacao.repository.SessaoVotacaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SessaoVotacaoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessaoVotacaoService.class);
    private static final Integer TEMPO_DEFAULT = 1;

    private final SessaoVotacaoRepository repository;
    private final PautasService pautasService;

    @Autowired
    public SessaoVotacaoService(SessaoVotacaoRepository repository, PautasService pautasService) {
        this.repository = repository;
        this.pautasService = pautasService;
    }

    /**
     * Se a sessao votacao é valida inicia a contagem para o encerramento da mesma.
     *
     * @param abrirSessaoVotacaoDTO - @{@link AbrirSessaoVotacaoDTO}
     * @return - @{@link SessaoVotacaoDTO}
     */
    @Transactional
    public SessaoVotacaoDTO abrirSessaoVotacao(AbrirSessaoVotacaoDTO abrirSessaoVotacaoDTO) {
        LOGGER.debug("Abrindo a sessao de votacao para a pauta {}", abrirSessaoVotacaoDTO.getIdPauta());

        isValidaAbrirSessao(abrirSessaoVotacaoDTO);

        SessaoVotacaoDTO dto = new SessaoVotacaoDTO(
                null,
                LocalDateTime.now(),
                calcularTempo(abrirSessaoVotacaoDTO.getTempo()),
                Boolean.TRUE);

        return salvar(dto);
    }

    /**
     * valida se os dados para iniciar uma validacao são consistentes
     * e ja estao persistidos na base de dados
     *
     * @param abrirSessaoVotacaoDTO - @{@link AbrirSessaoVotacaoDTO}
     * @return - boolean
     */
    @Transactional(readOnly = true)
    public boolean isValidaAbrirSessao(AbrirSessaoVotacaoDTO abrirSessaoVotacaoDTO) {
        if (pautasService.isPautaValida(abrirSessaoVotacaoDTO.getIdPauta())) {
            return Boolean.TRUE;
        } else {
            throw new NotFoundException("Pauta não localizada idPauta" + abrirSessaoVotacaoDTO.getIdPauta());
        }
    }

    /**
     * busca sessoes em andamento, se houver, os dados sao retornados para o validador de tempo
     *
     * @return - List<@{@link SessaoVotacaoDTO}>
     */
    @Transactional(readOnly = true)
    public List<SessaoVotacaoDTO> buscarSessaoesEmAndamento() {
        LOGGER.debug("Buscando sessoes em andamento");
        List<SessaoVotacaoDTO> list = repository.buscarTodasSessoesEmAndamento(Boolean.TRUE)
                .stream()
                .map(SessaoVotacaoDTO::toDTO)
                .collect(Collectors.toList());

        return list
                .stream()
                .filter(dto -> dto.getDataHoraFim().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    /**
     * Se houver sessao de votacao com o tempo expirado,
     * a flag ativo é setado como FALSE e persistido a alteracao na base de dados.
     *
     * @param dto - @{@link SessaoVotacaoDTO}
     */
    @Transactional
    public void encerraoSessaoVotacao(SessaoVotacaoDTO dto) {
        LOGGER.debug("Encerrando sessao com tempo de duracao expirado {}", dto.getId());
        dto.setAtiva(Boolean.FALSE);
        salvar(buscarSessaoVotacaoPeloOID(dto.getId()));
    }

    /**
     * @param id - @{@link SessaoVotacaoDTO} ID
     * @return - @{@link SessaoVotacaoDTO}
     */
    @Transactional(readOnly = true)
    public SessaoVotacaoDTO buscarSessaoVotacaoPeloID(Integer id) {
        Optional<SessaoVotacao> optionalSessaoVotacao = repository.findById(id);
        if (!optionalSessaoVotacao.isPresent()) {
            LOGGER.error("Sessao de votacao nao localizada para o id {}", id);
            throw new NotFoundException("Sessão de votação não localizada para o id " + id);
        }
        return SessaoVotacaoDTO.toDTO(optionalSessaoVotacao.get());
    }

    /**
     * Conforme o ID informado se existir uma sessao e com a tag ativa igual a TRUE
     * entao e considerada como valida para votacao.
     *
     * @param id - @{@link SessaoVotacao} ID
     * @return - boolean
     */
    @Transactional(readOnly = true)
    public boolean isSessaoVotacaoValida(Integer id) {
        return repository.existsByIdAndAndAtiva(id, Boolean.TRUE);
    }

    /**
     * @param id - @{@link SessaoVotacao} ID
     * @return - boolean
     */
    @Transactional(readOnly = true)
    public boolean isSessaoVotacaoExiste(Integer id) {
        if (repository.existsById(id)) {
            return Boolean.TRUE;
        } else {
            LOGGER.error("Sessao de votacao nao localizada para o id {}", id);
            throw new NotFoundException("Sessão de votação não localizada para o id " + id);
        }
    }

    /**
     * Conforme o ID informado se existir uma sessao e com tag ativa igual a FALSE
     * entao e considerada como valida para contagem
     *
     * @param id - @{@link SessaoVotacao} ID
     * @return - boolean
     */
    @Transactional(readOnly = true)
    public boolean isSessaoValidaParaContagem(Integer id) {
        return repository.existsByIdAndAndAtiva(oid, Boolean.FALSE);
    }

    /**
     * Com base no LocalDateTime inicial e calculada a LocalDateTime final somando-se o
     * tempo em minutos informado na chamada do servico.
     *
     * Se o tempo nao for informado ou for informado com valor 0,
     * entao é considerado o tempo de 1 minuto como default.
     *
     * @param tempo - tempo em minutos
     * @return - localDateTime
     */
    private LocalDateTime calcularTempo(Integer tempo) {
        if (tempo != null && tempo != 0) {
            return LocalDateTime.now().plusMinutes(tempo);
        } else {
            return LocalDateTime.now().plusMinutes(TEMPO_DEFAULT);
        }
    }

    /**
     * @param dto - @{@link SessaoVotacaoDTO}
     * @return - @{@link SessaoVotacaoDTO}
     */
    @Transactional
    public SessaoVotacaoDTO salvar(SessaoVotacaoDTO dto) {
        LOGGER.debug("Salvando a sessao de votacao");
        if (Optional.ofNullable(dto).isPresent()) {
            return SessaoVotacaoDTO.toDTO(repository.save(SessaoVotacaoDTO.toEntity(dto)));
        }
        return null;
    }
}
