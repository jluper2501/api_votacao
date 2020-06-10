package br.com.votacao.votacao.schedule;

import br.com.votacao.votacao.dto.SessaoVotacaoDTO;
import br.com.votacao.votacao.service.SessaoVotacaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component

public class DuracaoComponent {
    private static final Logger LOGGER = LoggerFactory.getLogger(DuracaoComponent.class);

    private final SessaoVotacaoService sessaoVotacaoService;

    public DuracaoComponent(SessaoVotacaoService sessaoVotacaoService) {
        this.sessaoVotacaoService = sessaoVotacaoService;
    }

    @Scheduled(cron = "15 * * * * *")
    private void teste() {
        LOGGER.debug("Contador de tempo sendo excutado...");
        List<SessaoVotacaoDTO> list = sessaoVotacaoService.buscarSessoesEmAndamento();
        LOGGER.debug("Quantidade de sessoes abertas  = {}", list.size());
        list.forEach(dto -> {
            LOGGER.debug("Sessao encerrada {}", dto.getOid());
            if (dto.getAtiva()) {
                sessaoVotacaoService.encerraSessaoVotacao(dto);
            }
        });
    }
}
