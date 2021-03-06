package com.maquinadebusca.app.model.service;

import com.maquinadebusca.app.model.Consulta;
import com.maquinadebusca.app.model.EntradaRanking;
import com.maquinadebusca.app.model.IndiceInvertido;
import com.maquinadebusca.app.model.TermoConsulta;

import Util.StopWords;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ProcessadorConsultaService {

	@Autowired
	TermoDocumentoService ts;

	@Autowired
	DocumentoService ds;

	@Autowired
	IndiceInvertidoService iis;

	@Autowired
	IndexadorService is;

	private Map<String, EntradaRanking> mergeListasInvertidas = new Hashtable();

	public ProcessadorConsultaService() {
	}

	public Consulta processarConsulta(String textoConsulta) {
		this.mergeListasInvertidas = new Hashtable();
		StopWords sw = new StopWords();
		Consulta consulta = new Consulta(sw.retiraStopWords(textoConsulta));
		this.iniciarTermosConsulta(consulta);
		this.processarListasInvertidas(consulta);
		this.computarSimilaridade();
		consulta.setRanking(this.getRanking());
		return consulta;
	}

	public List<EntradaRanking> processarConsultaAplicacao(String textoConsulta) {
		this.mergeListasInvertidas = new Hashtable();
		StopWords sw = new StopWords();
		Consulta consulta = new Consulta(sw.retiraStopWords(textoConsulta));
		this.iniciarTermosConsulta(consulta);
		this.processarListasInvertidas(consulta);
		this.computarSimilaridade();
		return this.getRanking();
	}

	public void iniciarTermosConsulta(Consulta consulta) {
		String visaoConsulta = consulta.getVisao();
		String[] termos = visaoConsulta.split(" ");
		for (String termo : termos) {
			if (!termo.equals("")) {
				int f = is.frequencia(termo.trim(), termos);
				double idf = ts.getIdf(termo.trim());
				TermoConsulta termoConsulta = new TermoConsulta(termo.trim(), f, idf);
				consulta.adicionarTermoConsulta(termoConsulta);
			}
		}
	}

	public void processarListasInvertidas(Consulta consulta) {

		List<TermoConsulta> termosConsulta = consulta.getTermosConsulta();
		for (TermoConsulta termoConsulta : termosConsulta) {
			List<IndiceInvertido> entradasIndiceInvertido = iis.getEntradasIndiceInvertido(termoConsulta.getTexto());
			for (IndiceInvertido entradaIndiceInvertido : entradasIndiceInvertido) {
				if (this.mergeListasInvertidas.containsKey(entradaIndiceInvertido.getDocumento().getUrl())) {
					EntradaRanking entradaRanking = this.mergeListasInvertidas
							.get(entradaIndiceInvertido.getDocumento().getUrl());
					entradaRanking.adicionarProdutoPesos(termoConsulta.getPeso() * entradaIndiceInvertido.getPeso());
				} else {
					EntradaRanking entradaRanking = new EntradaRanking();
					entradaRanking.setUrl(entradaIndiceInvertido.getDocumento().getUrl());
					entradaRanking.setTitulo(entradaIndiceInvertido.getDocumento().getTitulo()); 
					entradaRanking.setDescricao(entradaIndiceInvertido.getDocumento().getDescricao());
					entradaRanking.adicionarProdutoPesos(termoConsulta.getPeso() * entradaIndiceInvertido.getPeso());
					entradaRanking.setSomaQuadradosPesosDocumento(
							entradaIndiceInvertido.getDocumento().getSomaQuadradosPesos());
					entradaRanking.setSomaQuadradosPesosConsulta(consulta.getSomaQuadradosPesos());
					
					this.mergeListasInvertidas.put(entradaIndiceInvertido.getDocumento().getUrl(), entradaRanking);
				}
			}
			
			int i = 0;  
		} 
	}

	public void computarSimilaridade() {
		Collection<EntradaRanking> ranking = this.mergeListasInvertidas.values();
		for (EntradaRanking entradaRanking : ranking) {
			entradaRanking.computarSimilaridade();
		}
	}

	public List<EntradaRanking> getRanking() {
		List<EntradaRanking> resp = new ArrayList<EntradaRanking>();
		Collection<EntradaRanking> ranking = this.mergeListasInvertidas.values();

		for (EntradaRanking entradaRanking : ranking) {
			resp.add(entradaRanking);
		}

		return ordenaRanking(resp);
	}

	private List<EntradaRanking> ordenaRanking(List<EntradaRanking> ranking) {
		List<EntradaRanking> resp = new ArrayList<EntradaRanking>(ranking.size());
		EntradaRanking auxi = new EntradaRanking();

		int i = 0;
		while (ranking.size() > 0 && i < 20){
			auxi = ranking.get(0);
			for (int j = 1; j < ranking.size(); j++) {
				if (auxi.getSimilaridade() < ranking.get(j).getSimilaridade()) {
					auxi = ranking.get(j);
				}
			}
			resp.add(auxi);
			ranking.remove(auxi);
			i++;
		}

		return resp;
	}
}
