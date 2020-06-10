package br.com.votacao.votacao.tests;

import br.com.votacao.votacao.entity.Pautas;
import br.com.votacao.votacao.repository.PautasRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PautasTest {

    @Autowired
    private PautasRepository repository;

    @Test
    public void persistirPautas() {
        Pautas pautas = new Pautas(null, "Teste descricao da pauta");
        this.repository.save(pautas);
        assertThat(pautas.getId()).isNotNull();
        assertThat(pautas.getDescricao()).isEqualTo("Teste descricao da pauta");

    }

    @Test
    public void retornarVerdadeiro() {
        Pautas pautas = new Pautas(null, "Teste descricao da pauta");
        this.repository.save(pautas);
        assertThat(this.repository.existsById(pautas.getId())).isTrue();
    }

    @Test
    public void retornarFalso() {
        Pautas pautas = new Pautas(null, "Teste descricao da pauta");
        this.repository.save(pautas);
        assertThat(this.repository.existsById(3)).isFalse();
    }
}
