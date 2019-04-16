/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maquinadebusca.app.model.service;

import com.maquinadebusca.app.model.Documento;
import com.maquinadebusca.app.model.Link;
import com.maquinadebusca.app.model.repository.DocumentoRepository;
import com.maquinadebusca.app.model.repository.LinkRepository;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author davya
 */

@Service
public class ColetorService {

	private List<Link> urlsSementes;
	private List<Link> links; 
	private List<String> urlsDisallow;

	@Autowired
	private DocumentoService docService;

	@Autowired
	private LinkService linkService;
	
	@Autowired
	private HostService hostService;

	public ColetorService() {
		urlsSementes = new ArrayList<Link>(); 
		links = new ArrayList<Link>(); 
	}

	public List<Documento> executar() {
		try {
			urlsSementes = linkService.getLinkslSementes();
			for (Link url : urlsSementes) { 
				this.coletar(url);
			}

			boolean existeLink = false;
			int i = 0;
			if (links.size() > 0) {
				existeLink = true;
			}
			while (existeLink) {
				Link link = links.get(i); 
				this.coletar(link);
				i++;
				if (i >= links.size() || docService.getDocumentos().size() > 10) {
					existeLink = false;
				}
			}

		} catch (Exception e) {
			System.out.println("\n\n\n Erro ao executar o serviço de coleta! \n\n\n");
			e.printStackTrace();
		}

		return docService.getDocumentos();
	}  

	public Documento coletar(Link link) throws InterruptedException {
		String urlDocumento = link.getUrl();
		Documento documento = new Documento();

		try {
			link = linkService.verificaUltimaColetaURL(urlDocumento);
			
			if (link.isPodeColetar()) {
				urlsDisallow = recuperaRobots(urlDocumento);
				Document d = Jsoup.connect(urlDocumento).get();
				Elements urls = d.select("a[href]");

				documento.setUrl(urlDocumento);
				documento.setTexto(d.html());
				documento.setVisao(d.text());
				 
				link.setUltimaColeta(LocalDateTime.now()); 
				  
				int i = 0;
				for (Element url : urls) {
					i++;
					String u = url.attr("abs:href");
					Link linkEncontrado = null;
					if ((!u.equals("")) && (u != null) && verificaUrlAllow(u)) {
						if (!linkService.verificaLinkExistente(u) && !u.equals(urlDocumento)) {
							linkEncontrado = new Link();
							linkEncontrado.setUrl(u);
							linkEncontrado.setUltimaColeta(null); 
							links.add(linkEncontrado);
							documento.addLink (linkEncontrado); 
						} 
					}
				}
				System.out.println("Número de links coletados: " + i); 
				
				URL urlH = new URL(urlDocumento); 
				hostService.addLink(documento, urlH.getHost()); 
				
				if(link.getDocumento() == null) {
					link.setDocumento(documento);
					linkService.addLink(link);
				}
			}
		} catch (Exception e) {
			System.out.println("\n\n\n Erro ao coletar a página! \n\n\n");
			e.printStackTrace();
		}

		new Thread();
		Thread.sleep(1000);
		return documento;
	} 
	
	private boolean verificaUrlAllow(String u) {
		for (String url : urlsDisallow) {
			if (u.contains(url)) {
				return false;
			}
		}
		return true;
	}
  
	public List<String> recuperaRobots(String url_str) throws MalformedURLException {
		URL url = new URL(url_str);
		String host = url.getProtocol() + "://" + url.getHost();
		List<String> urlsDisallow = null;
		try {
			urlsDisallow = new ArrayList<String>();
			Document d = Jsoup.connect(host.concat("/robots.txt")).get();
			String[] urlsDisallowStr = d.text().split("Disallow:");
			for (String urlD : urlsDisallowStr) {
				if (!urlD.contains("Allow") && !urlD.isEmpty()) {
					urlsDisallow.add(host.concat(urlD.trim()));
				}
			}
		} catch (HttpStatusException e) {
			if (e.getStatusCode() == 404) {
				System.out.println("\n\n\n Erro ao coletar a página Robots TXT! Página não encontrada! \n\n\n");
				return urlsDisallow;
			}
		} catch (Exception e) {
			System.out.println("\n\n\n Erro ao coletar a página Robots TXT! \n\n\n");
			e.printStackTrace();
		}

		return urlsDisallow;
	}

	

	
}