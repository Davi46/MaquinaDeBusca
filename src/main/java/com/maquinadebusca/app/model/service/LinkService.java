package com.maquinadebusca.app.model.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.omg.CORBA.portable.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maquinadebusca.app.model.Documento;
import com.maquinadebusca.app.model.Link;
import com.maquinadebusca.app.model.ResultadoPagina;
import com.maquinadebusca.app.model.repository.LinkRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import org.springframework.data.domain.Slice;

@Service
public class LinkService {

	private static final int SIZEPAG = 3;
	@Autowired
	private LinkRepository lr;

	public Link saveLink(Link link) {
		return lr.save(link);
	}

	public List<Link> getLinks() {
		Iterable<Link> Links = lr.findAll();
		List<Link> resposta = new LinkedList<Link>();
		for (Link Link : Links) {
			resposta.add(Link);
		}
		return resposta;
	}

	public Link getLinkById(long id) {
		Link Link = lr.findById(id);
		return Link;
	}

	public Link verificaUltimaColetaURL(String urlDocumento) {
		Link link = getByLink(urlDocumento);
		if (link == null) {
			link = new Link();
			link.setPodeColetar(true);
		}

		if (link.getUltimaColeta() != null) {
			link.setPodeColetar(false);
		} else {
			link.setPodeColetar(true);
		}

		link.setUrl(urlDocumento);

		return link;
	}

	public Boolean verificaLinkExistente(String url) {
		for (Link link : getLinks()) {
			if (link.getUrl().equals(url)) {
				return true;
			}
		}

		return false;
	}

	public Link getByLink(String url) {
		Link l = lr.findByUrl(url);
		return l;
	}

	public Link salvarLink(Link link) throws Exception {
		Link l = null;
		try {
			l = lr.save(link);
		} catch (Exception e) {
			System.out.println("\n>>> Não foi possível salvar o link informado no banco de dados.\n");
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return l;
	}

	public List<Link> salvarLinks(Iterable<Link> links) throws Exception {
		List<Link> l = null;
		try {
			l = lr.saveAll(links);
		} catch (Exception e) {
			System.out.println("\n>>> Não foi possível salvar o link informado no banco de dados.\n");
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return l;
	}

	public List<Link> getLinkslSementes() {
		List<Link> linksSementes = new ArrayList<Link>();
		;
		for (Link link : getLinks()) {
			if (link.getDocumento() == null) {
				linksSementes.add(link);
			}
		}

		return linksSementes;
	}

	public List<Link> getUrlsColetar() {
		List<Link> linksSementes = new ArrayList<Link>();
		;
		for (Link link : getLinks()) {
			if (link.getUltimaColeta() == null) {
				linksSementes.add(link);
			}
		}

		return linksSementes;
	}

	public Link getProxUrlColetar() {
		for (Link link : getLinks()) {
			if (link.getUltimaColeta() == null) {
				return link;
			}
		}

		return null;

	}

	public List<Link> encontrarLinkUrl(String url) {
		return lr.findByUrlIgnoreCaseContaining(url);
	}

	public List<Link> listarEmOrdemAlfabetica() {
		return lr.getInLexicalOrder();
	}

	public String buscarPagina() {
		Slice<Link> pagina = null;
		Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "url"));

		while (true) {
			pagina = lr.getPage(pageable);
			int numeroDaPagina = pagina.getNumber();
			int numeroDeElementosNaPagina = pagina.getNumberOfElements();
			int tamanhoDaPagina = pagina.getSize();
			System.out.println("\n\nPágina: " + numeroDaPagina + " Número de Elementos: " + numeroDeElementosNaPagina
					+ " Tamaho da Página: " + tamanhoDaPagina);
			List<Link> links = pagina.getContent();
			links.forEach(System.out::println);
			if (!pagina.hasNext()) {
				break;
			}
			pageable = pagina.nextPageable();
		}
		return "{\"resposta\": \"Ok\"}";
	}

	public List<ResultadoPagina> retornaPaginas() {
		Slice<Link> pagina = null;
		List<ResultadoPagina> resultado = new ArrayList<ResultadoPagina>();
		Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "url"));

		while (true) {
			ResultadoPagina resultadoPag = new ResultadoPagina();
			pagina = lr.getPage(pageable);
			resultadoPag.setTamanhoPag(pagina.getSize());
			resultadoPag.setNumeroElementos(pagina.getNumberOfElements());
			resultadoPag.setNumeroPag(pagina.getNumber());
			resultadoPag.setLinks(pagina.getContent());
			resultado.add(resultadoPag);
			if (!pagina.hasNext()) {
				break;
			}
			pageable = pagina.nextPageable();
		}
		return resultado;
	}

	public ResultadoPagina getPag(int pag) {
		Slice<Link> pagina = null;
		Pageable pageable = PageRequest.of(pag, SIZEPAG, Sort.by(Sort.Direction.DESC, "url"));

		ResultadoPagina resultadoPag = new ResultadoPagina();
		pagina = lr.getPage(pageable);
		if (pagina != null) {
			resultadoPag.setTamanhoPag(pagina.getSize());
			resultadoPag.setNumeroElementos(pagina.getNumberOfElements());
			resultadoPag.setNumeroPag(pagina.getNumber());
			resultadoPag.setLinks(pagina.getContent());
		}

		return resultadoPag;
	}

	public List<Link> pesquisarLinkPorIntervaloDeIdentificacao(Long id1, Long id2) {
		return lr.findLinkByIdRange(id1, id2);
	}

	public Long contarLinkPorIntervaloDeIdentificacao(Long id1, Long id2) {
		return lr.countLinkByIdRange(id1, id2);
	}

	public List<Link> pesquisarLinkPorIntervaloDeDataUltimaColeta(Date date1, Date date2) {
		return lr.LinkByDateColetaRange(date1, date2);
	}

	public int atualizarDataUltimaColeta(String host, LocalDateTime dataUltimaColeta) {
		return lr.updateLastCrawlingDate(dataUltimaColeta, host);
	}
	
	public void remove(Link link) {
		lr.remover(link.getId());  
	}
}
