package br.com.votacao.votacao.tests;

import br.com.votacao.votacao.entity.Associados;
import br.com.votacao.votacao.repository.AssociadosRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AssociadosTest {

    @Autowired
    private AssociadosRepository repository;

    @Test
    public void persistirAssociados() {
        Associados associados = new Associados(null, "123123", 1);
        this.repository.save(associados);
        assertThat(associados.getId()).isNotNull();
        assertThat(associados.getCpfAssociado()).isEqualTo("123123");
    }

    @Test
    public void retornarUmAssociados() {
        Associados associados = new Associados(null, "123123", 1);
        this.repository.save(associados);
        assertThat(this.repository.findById(1)).isNotNull();
    }

    @Test
    public void retonarVerdadeiro() {
        Associados associados = new Associados(null, "123123", 1);
        this.repository.save(associados);
        assertThat(this.repository.existsByCpfAssociadoAndIdPauta("123123", 1)).isTrue();
    }

    @Test
    public void retonarFalso() {
        Associados associados = new Associados(null, "123123", 12);
        this.repository.save(associados);
        assertThat(this.repository.existsByCpfAssociadoAndIdPauta("123123", 1)).isFalse();
    }
}