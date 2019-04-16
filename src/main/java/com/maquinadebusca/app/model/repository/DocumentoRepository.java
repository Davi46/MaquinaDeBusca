package com.maquinadebusca.app.model.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.maquinadebusca.app.model.Documento;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {
	@Override
	List<Documento> findAll();

	Documento findById(long id);
}
