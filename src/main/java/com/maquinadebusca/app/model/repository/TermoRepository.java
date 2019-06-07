package com.maquinadebusca.app.model.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.maquinadebusca.app.model.TermoDocumento;

public interface TermoRepository extends JpaRepository<TermoDocumento, Long> {

	@Override
	List<TermoDocumento> findAll();

	TermoDocumento findById(long id);

	@Override
	TermoDocumento save(TermoDocumento termo);
}
