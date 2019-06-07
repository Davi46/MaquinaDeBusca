package com.maquinadebusca.app.model.service;

import com.maquinadebusca.app.model.Documento;
import com.maquinadebusca.app.model.TermoDocumento;
import com.maquinadebusca.app.model.repository.DocumentoRepository;
import com.maquinadebusca.app.model.repository.TermoRepository;
import java.util.Hashtable;
import org.springframework.stereotype.Service;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IndexadorService {

	private Hashtable hashTermos;

	@Autowired
	DocumentoRepository dr;

	@Autowired
	TermoRepository tr;

	public IndexadorService() {
		this.hashTermos = new Hashtable();
	}

	@Transactional
	public boolean criarIndice() {
		List<Documento> documentos = this.getDocumentos();
		for (Documento documento : documentos) {
			documento.setFrequenciaMaxima(0L);
			documento.setSomaQuadradosPesos(0L);  
			documento = dr.save(documento);
			this.indexar(documento);
		}

		return true;
	}

	public void indexar(Documento documento) {
		int i;

		String visaoDocumento = documento.getVisao();
		String[] termos = visaoDocumento.split(" ");// Cria um vetor por palavras da visao do documento
		for (String termo : termos) {
			if (!termo.equals("")) { // Termo diferente que vazio
				TermoDocumento termoDocumento = this.getTermo(termo);
				int f = this.frequencia(termoDocumento.getTexto(), termos);
				if (f > documento.getFrequenciaMaxima()) {
					documento.setFrequenciaMaxima(f);
				}
				termoDocumento.inserirEntradaIndiceInvertido(documento, f);
			}
		}
	}

	/**
	 * Método responsável por retornar um TermoDocumento, caso já exista um termo
	 * criado com o mesmo texto recebido, o mesmo ~e retornado caso contrario é
	 * criado um novo termo com o texto recebido.
	 * 
	 * @param texto palavra recebida a ser verificada como termo
	 * @return termo retorna um objeto do tipo TermoDocumento
	 */
	public TermoDocumento getTermo(String texto) {
		TermoDocumento termo;

		if (this.hashTermos.containsKey(texto)) {
			termo = (TermoDocumento) this.hashTermos.get(texto);
		} else {
			termo = new TermoDocumento();
			termo.setTexto(texto);
			termo.setN(0L);
			termo = tr.save(termo);
			this.hashTermos.put(texto, termo);
		}

		return termo;
	}

	/**
	 * Método responsável por calcular a frequuência de um termo dentro de um vetor
	 * de termos.
	 * 
	 * @param termo  a ser verificado no vetor
	 * @param termos vetor de termos
	 * @return frequencia que o termo é encontrado dentro de Termos
	 */
	public int frequencia(String termo, String[] termos) {
		int i, contador = 0; 
		for (i = 0; i < termos.length; i++) {
			if (!termos[i].equals("")) {
				if (termos[i].equalsIgnoreCase(termo)) {
					contador++;
					termos[i] = "";
				}
			}
		}

		return contador;
	}
 
	public List<Documento> getDocumentos() {
		Documento documento;
		List<Documento> documentos = new LinkedList();

		documento = new Documento();
		documento.setUrl("www.1.com.br");
		documento.setTexto("to do is to be to be is to do");
		documento.setVisao("to do is to be to be is to do");
		documentos.add(documento);

		documento = new Documento();
		documento.setUrl("www.2.com.br");
		documento.setTexto("to be or not to be i am what i am");
		documento.setVisao("to be or not to be i am what i am");
		documentos.add(documento);

		documento = new Documento();
		documento.setUrl("www.3.com.br");
		documento.setTexto("i think therefore i am do be do be do");
		documento.setVisao("i think therefore i am do be do be do");
		documentos.add(documento);
		documento = new Documento();
		documento.setUrl("www.4.com.br");
		documento.setTexto("do do do da da da let it be let it be");
		documento.setVisao("do do do da da da let it be let it be");
		documentos.add(documento);

		return documentos;
	}

}
