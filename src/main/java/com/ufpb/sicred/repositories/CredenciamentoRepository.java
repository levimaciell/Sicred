package com.ufpb.sicred.repositories;

import com.ufpb.sicred.entities.Credenciamento;
import com.ufpb.sicred.entities.Inscricao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredenciamentoRepository extends JpaRepository<Credenciamento, Long> {

    Credenciamento findByInscricao(Inscricao inscricao);

}
