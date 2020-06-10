package br.com.votacao.votacao.repository;

import br.com.votacao.votacao.entity.SessaoVotacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SessaoVotacaoRepository extends JpaRepository<SessaoVotacao, Integer> {
    List<SessaoVotacao> buscarTodasSessoesEmAndamento(Boolean ativo);

    Boolean existsByIdAndAndAtiva(Integer id, Boolean ativa);

    Boolean existsById(Integer id);
}
