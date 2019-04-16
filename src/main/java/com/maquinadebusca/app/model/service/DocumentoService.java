package com.maquinadebusca.app.model.service;
 
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maquinadebusca.app.model.Documento;
import com.maquinadebusca.app.model.repository.DocumentoRepository;

@Service
public class DocumentoService{

	@Autowired
	private DocumentoRepository dr;
	
	public Documento addDocumento(Documento doc) {
		return dr.save(doc);
	}
	
	public List<Documento> getDocumentos() {
		Iterable<Documento> documentos = dr.findAll();
		List<Documento> resposta = new LinkedList();
		for (Documento documento : documentos) {
			resposta.add(documento);
		}
		return resposta;
	}
	
	public Documento getDocumentoById(long id) {
		Documento documento = dr.findById(id);
		return documento;
	}  
}
