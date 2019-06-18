package com.maquinadebusca.app.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set; 
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank; 
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Host implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@NotBlank
	@Column(unique=true, nullable=false, length = 200) 
	private String host;
	 
	private int qtdPaginas;
	
	@OneToMany(mappedBy = "host", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<Documento> documentos;

	public Host(Long id, String host, Integer qtdPaginas, Set<Link> links) {
		this.id = id;
		this.host = host;
		this.qtdPaginas = qtdPaginas;
		this.documentos = new HashSet();
	}

	public Host() {
		this.documentos = new HashSet();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getQtdPaginas() {
		return qtdPaginas;
	}

	public void setQtdPaginas(int qtdPaginas) {
		this.qtdPaginas = qtdPaginas;
	}

	public Set<Documento> getDocumentos() {
		return documentos;
	}

	public void setDocumento(Set<Documento> documento) {
		this.documentos = documento;
	} 
	
	public void addDocumento(Documento documento) {
		documento.setHost(this);
		this.documentos.add(documento);
	} 

	public void removeDocumento(Documento documento) {
		documento.setHost(null);
		documentos.remove(documento);
	}
}
