package com.maquinadebusca.app.controller;

import com.maquinadebusca.app.mensagem.Mensagem;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.maquinadebusca.app.model.Usuario;
import com.maquinadebusca.app.model.service.IndexadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/indexador") // URL: http://localhost:8080/indexador
public class IndexadorController {

	@Autowired
	IndexadorService is;

	// URL: http://localhost:8080/indexador/indice
	@PostMapping(value = "/indice", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Mensagem> criarIndice() {
		ResponseEntity<Mensagem> resp = null;
		try {
			boolean confirmacao = is.criarIndice(); 
			if (confirmacao) {
				resp = new ResponseEntity<Mensagem>(new Mensagem("sucesso", "o índice invertido foi criado com sucesso", null),
						HttpStatus.CREATED);
			} else {
				resp = new ResponseEntity<Mensagem>(new Mensagem("erro", "o índice invertido não pode ser criado", null),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}catch (Exception e) {
			resp = new ResponseEntity<Mensagem>(new Mensagem("erro", "o índice invertido não pode ser criado",  e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return resp;
	}  
}