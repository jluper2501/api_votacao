package br.com.votacao.votacao.service;

import br.com.votacao.votacao.dto.*;
import br.com.votacao.votacao.exception.VotacaoNotFoundException;
import br.com.votacao.votacao.exception.SessaoFechadaException;
import br.com.votacao.votacao.exception.VotoInvalidoException;
import br.com.votacao.votacao.repository.VotacaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service

public class VotacaoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VotacaoService.class);

    private final VotacaoRepository repository;
    private final PautasService pautasService;
    private final SessaoVotacaoService sessaoVotacaoService;
    private final AssociadosService associadosService;

    @Autowired
    public VotacaoService(VotacaoRepository repository, PautasService pautasService, SessaoVotacaoService sessaoVotacaoService, AssociadosService associadosService) {
        this.repository = repository;
        this.pautasService = pautasService;
        this.sessaoVotacaoService = sessaoVotacaoService;
        this.associadosService = associadosService;
    }

    /**
     * metodo que realiza validacoes antes do voto ser computado
     * e persistido na base de dados
     *
     * @param dto - @{@link VotoDTO}
     * @return - boolean
     */
    @Transactional(readOnly = true)
    public boolean isValidaVoto(VotoDTO dto) {
        LOGGER.debug("Validando os dados para voto idSessao = {}, idPauta = {}, idAssiciado = {}", dto.getIdSessaoVotacao(), dto.getIdPauta(), dto.getCpfAssociado());

        if (!pautasService.isPautaValida(dto.getIdPauta())) {

            LOGGER.error("Pauta nao localizada para votacao idPauta {}", dto.getIdPauta());
            throw new NotFoundException("Pauta não localizada id: " + dto.getIdPauta());

        } else if (!sessaoVotacaoService.isSessaoVotacaoValida(dto.getIdSessaoVotacao())) {

            LOGGER.error("Tentativa de voto para sessao encerrada idSessaoVotacao {}", dto.getIdSessaoVotacao());
            throw new SessoEncerradaException("Sessão de votação já encerrada");

        } else if (!associadosService.isAssociadoPodeVotar(dto.getCpfAssociado())) {

            LOGGER.error("Associado nao esta habilitado para votar {}", dto.getCpfAssociado());
            throw new VotoInvalidoException("Não é possível votar mais de 1 vez na mesma pauta");

        } else if (!associadoService.isValidaParticipacaoAssociadoVotacao(dto.getCpfAssociado(), dto.getIdPauta())) {

            LOGGER.error("Associado tentou votar mais de 1 vez cpfAssociado {}", dto.getCpfAssociado());
            throw new VotoInvalidoException("Não é possível votar mais de 1 vez na mesma pauta");
        }

        return Boolean.TRUE;
    }

    /**
     * Se os dados informados para o voto, forem considerados validos
     * entao o voto é computado e persistido na base de dados.
     *
     * @param dto - @{@link VotoDTO}
     * @return - String
     */
    @Transactional
    public String votar(VotoDTO dto) {
        if (isValidaVoto(dto)) {
            LOGGER.debug("Dados validos para voto idSessao = {}, idPauta = {}, cpfAssiciado = {}", dto.getIdSessaoVotacao(), dto.getIdPauta(), dto.getCpfAssociado());

            VotacaoDTO votacaoDTO = new VotacaoDTO(null,
                    dto.getIdPauta(),
                    dto.getIdSessaoVotacao(),
                    dto.getVoto(),
                    null,
                    null);

            registrarVoto(votacaoDTO);

            registrarAssociadoVotou(dto);

            return "Voto validado";
        }
        return null;
    }

    /**
     * Com o voto computado. O associado e registrado na base de dados a fim de
     * evitar que o mesmo possa votar novamente na mesma sessao de votacao e na mesma pauta.
     *
     * @param dto - @{@link VotoDTO}
     */
    @Transactional
    public void registrarAssociadoVotou(VotoDTO dto) {
        AssociadoDTO associadoDTO = new AssociadoDTO(null, dto.getCpfAssociado(), dto.getIdPauta());
        associadoService.salvarAssociado(associadoDTO);
    }

    /**
     * @param dto - @{@link VotacaoDTO}
     */
    @Transactional
    public void registrarVoto(VotacaoDTO dto) {
        LOGGER.debug("Salvando o voto para idPauta {}", dto.getIdPauta());
        repository.save(VotacaoDTO.toEntity(dto));
    }

    /**
     * Faz a contagem dos votos positivos e negativos para determinada sessao e pauta de votacao.
     *
     * @param idPauta         - @{@link br.com.votacao.votacao.entity.Pautas} ID
     * @param idSessaoVotacao - @{@link br.com.votacao.votacao.entity.SessaoVotacao} ID
     * @return - @{@link VotacaoDTO}
     */
    @Transactional(readOnly = true)
    public VotacaoDTO buscarResultadoVotacao(Integer idPauta, Integer idSessaoVotacao) {
        LOGGER.debug("Contagem dos votos para idPauta = {}, idSessaoVotacao = {}", idPauta, idSessaoVotacao);
        VotacaoDTO dto = new VotacaoDTO();

        dto.setIdPauta(idPauta);
        dto.setIdSessaoVotacao(idSessaoVotacao);

        dto.setQuantidadeVotosSim(repository.countVotacaoByIdPautaAndIdSessaoVotacaoAndVoto(idPauta, idSessaoVotacao, Boolean.TRUE));
        dto.setQuantidadeVotosNao(repository.countVotacaoByIdPautaAndIdSessaoVotacaoAndVoto(idPauta, idSessaoVotacao, Boolean.FALSE));

        return dto;
    }

    /**
     * Realiza a montagem dos objetos referente ao resultado de determinada sessao e pauta de votacao.
     *
     * Contagem somente e realizada apos a sessao encerrada.
     *
     * @param idPauta         - @{@link br.com.votacao.votacao.entity.Pautas} ID
     * @param idSessaoVotacao - @{@link br.com.votacao.votacao.entity.SessaoVotacao} ID
     * @return - @{@link ResultadoDTO}
     */
    @Transactional(readOnly = true)
    public ResultadoDTO buscarDadosResultadoVotacao(Integer idPauta, Integer idSessaoVotacao) {

        if (isValidaSeDadosExistem(idPauta, idSessaoVotacao) && sessaoVotacaoService.isSessaoValidaParaContagem(idSessaoVotacao)) {
            LOGGER.debug("Construindo o objeto de retorno do resultado para idPauta = {}, idSessaoVotacao = {}", idPauta, idSessaoVotacao);
            PautasDTO pautasDTO = pautasService.buscarPautaPeloID(idPauta);
            VotacaoDTO votacaoDTO = buscarResultadoVotacao(idPauta, idSessaoVotacao);
            return new ResultadoDTO(pautasDTO, votacaoDTO);
        }
        throw new NotFoundException("Sessão de votação ainda está aberta, não é possível obter a contagem do resultado.");
    }

    /**
     * @param idPauta         - @{@link br.com.votacao.votacao.entity.Pautas} ID
     * @param idSessaoVotacao - @{@link br.com.votacao.votacao.entity.SessaoVotacao} ID
     * @return - boolean
     */
    @Transactional(readOnly = true)
    public boolean isValidaSeDadosExistem(Integer idPauta, Integer idSessaoVotacao) {
        return sessaoVotacaoService.isSessaoVotacaoExiste(idSessaoVotacao) && pautasService.isPautaValida(idPauta);
    }
}
