package com.maquinadebusca.app.model.repository;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.maquinadebusca.app.model.Link;

public interface LinkRepository extends JpaRepository<Link, Long> {
	@Override
	List<Link> findAll();

	Link findById(long id);

	Link findByUrl(String url);

	@Override
	Link save(Link link);
	 
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM link WHERE id = :id", nativeQuery = true)
	void remover(@Param("id") Long id1);

	List<Link> findByUrlIgnoreCaseContaining(String url);

	@Query(value = "SELECT * FROM link ORDER BY url", nativeQuery = true)
	List<Link> getInLexicalOrder();

	@Query(value = "SELECT * FROM link", nativeQuery = true)
	public Slice<Link> getPage(Pageable pageable);

	@Query(value = "SELECT * FROM link WHERE id between ?1 and ?2", nativeQuery = true)
	List<Link> findLinkByIdRange(Long id1, Long id2);

	@Query(value = "SELECT COUNT(*) FROM Link WHERE id between :identificador1 and :identificador2", nativeQuery = true)
	Long countLinkByIdRange(@Param("identificador1") Long id1, @Param("identificador2") Long id2); 

	@Query(value = "SELECT * FROM link WHERE ultimaColeta between :identificador1 and :identificador2", nativeQuery = true)
	List<Link> LinkByDateColetaRange(@Param("identificador1") Date date1, @Param("identificador2") Date date2);
	
	@Transactional
	@Modifying
	@Query (value = "UPDATE link l SET l.ultimaColeta = :data WHERE l.url LIKE CONCAT('%',:host,'%')", nativeQuery = true)
	int updateLastCrawlingDate (@Param ("data") LocalDateTime ultimaColeta, @Param
	("host") String nomeHost);
}
