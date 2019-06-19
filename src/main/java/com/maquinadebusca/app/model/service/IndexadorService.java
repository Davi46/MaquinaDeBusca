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
	DocumentoService ds;

	@Autowired
	TermoRepository tr;
	
	@Autowired
	TermoDocumentoService tds;
	
	@Autowired
	IndiceInvertidoService iis;
	
	
	public IndexadorService() { 
		this.hashTermos = new Hashtable();
	}

	
	public boolean criarIndice() {
		this.hashTermos = new Hashtable();
		//Apagar registros de indice invertido e termo documento
		iis.removeAll();
		tds.removeAll();
		
		List<Documento> documentos = ds.getDocumentos();
		for (Documento documento : documentos) {
			documento.setFrequenciaMaxima(0L);
			documento.setSomaQuadradosPesos(0L);
			documento = ds.addDocumento(documento);
			this.indexar(documento, Long.valueOf(documentos.size()));
		}

		return true;
	}

	public void indexar(Documento documento, Long totalDocumentos) {
		int i;

		String visaoDocumento = documento.getVisao();
		// Cria um vetor por palavras da visao do documento
		String[] termos = visaoDocumento.split(" ");
		for (String termo : termos) {
			if (!termo.equals("") && !termo.equals("\n") && !termo.isEmpty()) { // Termo diferente que vazio
				TermoDocumento termoDocumento = this.getTermo(termo.trim(), documento);
				int f = this.frequencia(termoDocumento.getTexto(), termos);
				if (f > documento.getFrequenciaMaxima()) {
					documento.setFrequenciaMaxima(f);
				}
				double peso = recuperaPeso(totalDocumentos, termoDocumento.getN(), f); 
				documento.calculaQuadradosPeso(peso); 

				termoDocumento.inserirEntradaIndiceInvertido(documento, f, peso);
			}
		}
	}

	private double recuperaPeso(Long totalDocumentos, Long n, int frequencia) {
		double tf = recuperaTf(frequencia);
		double idf = recuperaIdf(totalDocumentos, n); 
		return tf * idf;
	}

	private double recuperaIdf(Long totalDocumentos, Long n) {
		try {
			if (totalDocumentos == 0 || n == 0L) {
				return 0;
			}

			return Math.log((totalDocumentos.doubleValue() / n.doubleValue()));
			
		} catch (Exception e) {
			return 0;
		} 
	}

	private double recuperaTf(int frequencia) {
		try {
			return 1 + Math.log(frequencia);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * Método responsável por retornar um TermoDocumento, caso já exista um termo
	 * criado com o mesmo texto recebido, o mesmo é retornado caso contrario é
	 * criado um novo termo com o texto recebido.
	 * 
	 * @param texto palavra recebida a ser verificada como termo
	 * @return termo retorna um objeto do tipo TermoDocumento
	 */
	public TermoDocumento getTermo(String texto, Documento doc) {
		TermoDocumento termo;

		if (this.hashTermos.containsKey(texto)) {
			termo = (TermoDocumento) this.hashTermos.get(texto);
		} else {
			termo = new TermoDocumento();
			termo.setTexto(texto);
			termo.setN(ds.recuperaNumeroDeDocumentosPorTermo(texto));
			termo = tr.save(termo);
			this.hashTermos.put(texto, termo);
		}

		return termo;
	}

	/**
	 * Método responsável por calcular a frequência de um termo dentro de um vetor
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

}
