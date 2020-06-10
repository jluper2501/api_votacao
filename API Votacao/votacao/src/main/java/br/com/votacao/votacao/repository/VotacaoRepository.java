package br.com.votacao.votacao.repository;

import br.com.votacao.votacao.entity.Votacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotacaoRepository extends JpaRepository<Votacao, Integer> {

    Integer countVotacaoByIdPautaAndIdSessaoVotacaoAndVoto(Integer idPauta, Integer idSessaoVotacao, Boolean voto);
}
