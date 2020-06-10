package br.com.votacao.votacao.tests;

import br.com.votacao.votacao.entity.SessaoVotacao;
import br.com.votacao.votacao.repository.SessaoVotacaoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SessaoVotacaoTest {

    @Autowired
    private SessaoVotacaoRepository repository;

    @Test
    public void persistirSessaoVotacao() {
        SessaoVotacao sessaoVotacao = new SessaoVotacao(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(1), Boolean.TRUE);
        this.repository.save(sessaoVotacao);
        assertThat(sessaoVotacao.getId()).isNotNull();
    }

    @Test
    public void retornarVerdadeiroParaListaDeSessoesVotacaoEmAndamento() {
        SessaoVotacao sessaoVotacao = new SessaoVotacao(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(1), Boolean.TRUE);
        this.repository.save(sessaoVotacao);
        assertThat(this.repository.buscarTodasSessoesEmAndamento(Boolean.TRUE)).isNotEmpty();
    }

    @Test
    public void retornarVerdadeiroParaListaVaziaDeSessoesVotacaoEmAndamento() {
        SessaoVotacao sessaoVotacao = new SessaoVotacao(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(1), Boolean.FALSE);
        this.repository.save(sessaoVotacao);
        assertThat(this.repository.buscarTodasSessoesEmAndamento(Boolean.TRUE)).isEmpty();
    }

    @Test
    public void retornarVerdadeiroParaBuscaSessaoExistenteAtiva() {
        SessaoVotacao sessaoVotacao = new SessaoVotacao(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(1), Boolean.TRUE);
        this.repository.save(sessaoVotacao);
        assertThat(this.repository.existsByIdAndAndAtiva(sessaoVotacao.getId(), Boolean.TRUE)).isTrue();
    }

    @Test
    public void retornarFalseParaBuscaSessaoExistenteAtiva() {
        this.repository.deleteAll();
        SessaoVotacao sessaoVotacao = new SessaoVotacao(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(1), Boolean.FALSE);
        this.repository.save(sessaoVotacao);
        assertThat(this.repository.existsByIdAndAndAtiva(sessaoVotacao.getId(), Boolean.TRUE)).isFalse();
    }
}