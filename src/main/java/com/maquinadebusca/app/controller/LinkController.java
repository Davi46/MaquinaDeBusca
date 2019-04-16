package com.maquinadebusca.app.controller; 

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping; 
import com.maquinadebusca.app.mensagem.Mensagem;
import com.maquinadebusca.app.model.Link;
import com.maquinadebusca.app.model.service.LinkService; 

@RestController
@RequestMapping("/link") // URL: http://localhost:8080/link
public class LinkController {
	@Autowired
	LinkService linkService;

	// URL: http://localhost:8080/link/listar
	@GetMapping(value = "/listar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List> listar() {
		return new ResponseEntity<List>(linkService.getLinks(), HttpStatus.OK);

	}

	// Request for: http://localhost:8080/link/listar/{id}
	@GetMapping(value = "/listar/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Link> listar(@PathVariable(value = "id") long id) {
		return new ResponseEntity<Link>(linkService.getLinkById(id), HttpStatus.OK);
	}

	// Request for: http://localhost:8080/link/listar/{url}
	@GetMapping(value = "/listar/url/{url}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Link> listar(@PathVariable(value = "url") String url) {
		return new ResponseEntity<Link>(linkService.getByLink(url), HttpStatus.OK);
	}

	// Request for: http://localhost:8080/link/add
	@PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> inserirLink(@RequestBody Link link) {
		try {
			link = linkService.salvarLink(link);
			if ((link != null) && (link.getId() > 0)) {
				return new ResponseEntity<Object>(link, HttpStatus.OK);
			} else {
				return new ResponseEntity<Object>(
						new Mensagem("erro", "não foi possível inserir o link informado no banco de dados", null),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			return new ResponseEntity<Object>(
					new Mensagem("erro", "não foi possível inserir o link informado no banco de dados", e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Request for: http://localhost:8080/link/addLinks
	@PostMapping(value = "/addLinks", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> inserirLinks(@RequestBody Iterable<Link> links) {
		try {
			Iterable<Link> l = linkService.salvarLinks(links);
			if (l != null) {
				return new ResponseEntity<Object>(l, HttpStatus.OK);
			} else {
				return new ResponseEntity<Object>(
						new Mensagem("erro", "não foi possível inserir o link informado no banco de dados", null),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			return new ResponseEntity<Object>(
					new Mensagem("erro", "não foi possível inserir o link informado no banco de dados", e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
