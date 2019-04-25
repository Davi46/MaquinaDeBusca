package com.maquinadebusca.app.model.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository; 
import com.maquinadebusca.app.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	@Override
	List<Usuario> findAll();

	Usuario findById(long id);
	
	//Usuario findByUser(String usuario);
}
