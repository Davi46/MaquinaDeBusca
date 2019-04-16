package com.maquinadebusca.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping; 
import com.maquinadebusca.app.model.service.DocumentoService;  

@RestController
@RequestMapping("/documento") // URL: http://localhost:8080/coletor
public class DocumentoController {
	@Autowired
	DocumentoService docService;

	// URL: http://localhost:8080/coletor/listar
	@GetMapping(value = "/listar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listar() {
		return new ResponseEntity (docService.getDocumentos(), HttpStatus.OK);

	}

	// Request for: http://localhost:8080/coletor/listar/{id}
	@GetMapping(value = "/listar/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listar(@PathVariable(value = "id") long id) {
		return  new ResponseEntity (docService.getDocumentoById(id), HttpStatus.OK);
	}
}
