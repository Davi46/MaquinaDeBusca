package com.maquinadebusca.app.model.repository;

import java.util.List; 
import org.springframework.data.jpa.repository.JpaRepository; 
import com.maquinadebusca.app.model.Host;

public interface HostRepository extends JpaRepository<Host, Long> {
	@Override
	List<Host> findAll();

	Host findById(long id); 
}
