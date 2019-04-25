package com.maquinadebusca.app.model.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.omg.CORBA.portable.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maquinadebusca.app.model.Documento;
import com.maquinadebusca.app.model.Link;
import com.maquinadebusca.app.model.repository.LinkRepository;

@Service
public class LinkService {

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
		if(link == null) {
			link = new Link(); 
			link.setPodeColetar(true);
		} 
		
		if (link.getUltimaColeta() != null) {
			link.setPodeColetar(false);
		}else {
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
	
	public List<Link> getLinkslSementes(){
		List<Link> links = getLinks();
		List<Link> linksSementes = new ArrayList<Link>(); ;
		for (Link link : links) { 
			if(link.getDocumento() == null) {
				linksSementes.add(link);
			}
		}
		
		return linksSementes;
	}
	
	public List<Link> getUrlsColetar(){
		List<Link> links = getLinks();
		List<Link> linksSementes = new ArrayList<Link>(); ;
		for (Link link : links) { 
			if(link.getUltimaColeta() == null) {
				linksSementes.add(link);
			}
		}
		
		return linksSementes;
	}
	
	public Link getProxUrlColetar() {
		for (Link link : getLinks()) { 
			if(link.getUltimaColeta() == null) {
				return link;
			}
		}
		
		return null;
		
	}

}
 