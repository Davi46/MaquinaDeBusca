package com.maquinadebusca.app.model.service;

import com.maquinadebusca.app.model.TermoDocumento;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.maquinadebusca.app.model.repository.TermoDocumentoRepository; 

@Service
public class TermoDocumentoService {

	@Autowired
	TermoDocumentoRepository tdr;
	
	@Autowired
	DocumentoService ds;

	public TermoDocumentoService() {
	}

	public TermoDocumento save(TermoDocumento termoDocumento) {
		return tdr.save(termoDocumento);
	}

	public double getIdf(String termoDocumento) {
		return tdr.getIdf(termoDocumento);
	}
	
	public void calculaN() {
		List<TermoDocumento> termos = new ArrayList<TermoDocumento>();
		termos = tdr.findAll();
		
		for (TermoDocumento termoDocumento : termos) {
			termoDocumento.setN(ds.recuperaNumeroDeDocumentosPorTermo(termoDocumento.getTexto()));
		}
	}
	
	public void removeAll() {
		tdr.removerAll();
	}

}