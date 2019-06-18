package com.maquinadebusca.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping; 
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.maquinadebusca.app.model.Documento;
import com.maquinadebusca.app.model.service.ColetorService; 
import java.net.MalformedURLException;
import java.util.List; 

@RestController
@RequestMapping("/coletor") // URL: http://localhost:8080/coletor
public class ColetorController {
	// URL: http://localhost:8080/coletor/iniciar

	@Autowired
	ColetorService cs;

	@GetMapping(value = "/iniciar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity iniciar() throws MalformedURLException {
		ResponseEntity<Object> resposta = null;
		List<Documento> docs = cs.executar ();
		if (!docs.isEmpty()) {
			resposta = new ResponseEntity<Object>(docs, HttpStatus.OK);
		} else {
			resposta = new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		}
		return resposta; 
	}  
}
