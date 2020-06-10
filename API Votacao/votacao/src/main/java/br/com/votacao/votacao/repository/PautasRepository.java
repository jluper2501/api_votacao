package br.com.votacao.votacao.repository;

import br.com.votacao.votacao.entity.Pautas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PautasRepository extends JpaRepository<Pautas, Integer> {
    Boolean existsById(Integer id) {
		return null;
	}
}
