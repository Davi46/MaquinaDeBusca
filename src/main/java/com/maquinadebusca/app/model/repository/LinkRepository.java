package com.maquinadebusca.app.model.repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maquinadebusca.app.model.Documento;
import com.maquinadebusca.app.model.Link;

public interface LinkRepository extends JpaRepository<Link, Long> {
	@Override
	List<Link> findAll();

	Link findById(long id);
	
	Link findByUrl(String url);
	
	@Override
	Link save(Link link);
	
	List<Link> findByUrlIgnoreCaseContaining (String url);
	
	 @Query (value = "SELECT * FROM link ORDER BY url", nativeQuery = true)
	 List<Link> getInLexicalOrder ();
	 
	 @Query (value = "SELECT * FROM link", nativeQuery = true)
	 public Slice<Link> getPage (Pageable pageable);
}
