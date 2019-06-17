package com.maquinadebusca.app.model;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

public class Documento implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@NotBlank
	private String url;

	@Lob
	@NotBlank
	private String texto;

	@Lob
	@NotBlank
	private String visao;

	@OneToMany(mappedBy = "documento", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<Link> links;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "host_id")
	private Host host;

	@NotBlank
	@Column(nullable = false, length = 200)   
	private String titulo;

	private double frequenciaMaxima;

	private double somaQuadradosPesos;

	@OneToMany(mappedBy = "documento", // Nome do atributo na classe IndiceInvertido.
			cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<IndiceInvertido> indiceInvertido;

	public Documento(String url, String texto, String visao, String titulo) {
		this.url = url;
		this.texto = texto;
		this.visao = visao;
		this.titulo = titulo;
		this.links = new HashSet();
	} 
	
	public double getFrequenciaMaxima() {
		return frequenciaMaxima;
	}
 

	public void setFrequenciaMaxima(double frequenciaMaxima) {
		this.frequenciaMaxima = frequenciaMaxima;
	}



	public double getSomaQuadradosPesos() {
		return somaQuadradosPesos;
	}



	public void setSomaQuadradosPesos(double somaQuadradosPesos) {
		this.somaQuadradosPesos = somaQuadradosPesos;
	}



	public List<IndiceInvertido> getIndiceInvertido() {
		return indiceInvertido;
	}



	public void setIndiceInvertido(List<IndiceInvertido> indiceInvertido) {
		this.indiceInvertido = indiceInvertido;
	}



	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Documento() {
		links = new HashSet();
	}

	public String getTexto() {
		return texto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getVisao() {
		return visao;
	}

	public void setVisao(String visao) {
		this.visao = visao;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Set<Link> getLinks() {
		return links;
	}

	public void setLinks(Set<Link> links) {
		this.links = links;
	}

	public void addLink(Link link) {
		link.setDocumento(this);
		this.links.add(link);
	}

	public void removeLink(Link link) {
		link.setDocumento(null);
		links.remove(link);
	}

	public Host getHost() {
		return host;
	}

	public void setHost(Host host) {
		this.host = host;
	}
	
	public void calculaQuadradosPeso(double peso) {
		somaQuadradosPesos += Math.sqrt(peso);
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 59 * hash + Objects.hashCode(this.id);
		hash = 59 * hash + Objects.hashCode(this.url);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		final Documento other = (Documento) obj;
		if (!Objects.equals(this.url, other.url)) {
			return false;
		}

		if (!Objects.equals(this.id, other.id)) {
			return false;
		}

		return true;
	}
}
