package com.maquinadebusca.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.maquinadebusca.app.model.Documento;
import com.maquinadebusca.app.model.Link;
import com.maquinadebusca.app.model.service.DocumentoService;

@RestController
@RequestMapping("/documento") // URL: http://localhost:8080/coletor
public class DocumentoController {
	@Autowired
	DocumentoService docService;

	// URL: http://localhost:8080/coletor/listar
	@GetMapping(value = "/listar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listar() {
		ResponseEntity<Object> resposta = null;
		List<Documento> docs = docService.getDocumentos();
		if (!docs.isEmpty()) {
			resposta = new ResponseEntity<Object>(docs, HttpStatus.OK);
		} else {
			resposta = new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		}
		return resposta;
	}

	// Request for: http://localhost:8080/coletor/listar/{id}
	@GetMapping(value = "/listar/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listar(@PathVariable(value = "id") long id) {
		ResponseEntity<Object> resposta = null;
		Documento doc = docService.getDocumentoById(id);
		if (doc != null) {
			resposta = new ResponseEntity<Object>(doc, HttpStatus.OK);
		} else {
			resposta = new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		}
		return resposta;
	}

	// Request for: http://localhost:8080/documento/encontrar/{pesquisa}
	@GetMapping(value = "/encontrar/{pesquisa}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> encontrarLink(@PathVariable(value = "pesquisa") String pesquisa) {
		ResponseEntity<Object> resposta = null;
		List<Documento> docs = docService.encontrarDocUrl(pesquisa);
		if (!docs.isEmpty()) {
			resposta = new ResponseEntity<Object>(docs, HttpStatus.OK);
		} else {
			resposta = new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		}
		return resposta;
	}
}
