package com.maquinadebusca.app.model.service;
 
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maquinadebusca.app.model.Documento;
import com.maquinadebusca.app.model.Link;
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

	public List<Documento> encontrarDocUrl(String pesquisa) {
		List<Object[]> objs = dr.getDocByText(pesquisa); 
		List<Documento> docs = new ArrayList<Documento>();
		for(Object[] row : objs){
			Documento doc = new Documento(); 
			doc.setTitulo(row[0].toString());
			doc.setUrl(row[1].toString());
			doc.setVisao(row[2].toString().substring(0, 240));  
			docs.add(doc);
		}
		
		return docs;
	}
	
	public long recuperaNumeroDeDocumentosPorTermo(String termo) {
		return dr.recuperaNumeroDeDocumentosPorTermo(termo);
	}
}
