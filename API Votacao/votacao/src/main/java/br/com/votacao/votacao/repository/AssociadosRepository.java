package br.com.votacao.votacao.repository;

import br.com.votacao.votacao.entity.Associados;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class AssociadosRepository extends JpaRepository<Associados, Integer> {
    Boolean existsByCpfAssociadoAndidPauta(String cpfAssociado, Integer idPauta) {
		return null;
	}
}
