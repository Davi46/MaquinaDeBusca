package com.maquinadebusca.app.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.maquinadebusca.app.mensagem.Mensagem;
import com.maquinadebusca.app.model.Link;
import com.maquinadebusca.app.model.ResultadoPagina;
import com.maquinadebusca.app.model.service.LinkService;

@RestController
@RequestMapping("/link") // URL: http://localhost:8080/link
public class LinkController {
	@Autowired
	LinkService linkService;

	// URL: http://localhost:8080/link/listar
	@GetMapping(value = "/listar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> listar() {
		ResponseEntity<Object> resposta = null;
		List<Link> links = linkService.getLinks();
		if (!links.isEmpty()) {
			resposta = new ResponseEntity<Object>(links, HttpStatus.OK);
		} else {
			resposta = new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		}
		return resposta;
	}

	// Request for: http://localhost:8080/link/listar/{id}
	@GetMapping(value = "/listar/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> listar(@PathVariable(value = "id") long id) {
		ResponseEntity<Object> resposta = null;

		Link link = linkService.getLinkById(id);
		if (link != null) {
			resposta = new ResponseEntity<Object>(link, HttpStatus.OK);
		} else {
			resposta = new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		}

		return resposta;
	}

	// Request for: http://localhost:8080/link/listar/{url}
	@GetMapping(value = "/listar/url/{url}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> listar(@PathVariable(value = "url") String url) {
		ResponseEntity<Object> resposta = null;

		Link link = linkService.getByLink(url);
		if (link != null) {
			resposta = new ResponseEntity<Object>(link, HttpStatus.OK);
		} else {
			resposta = new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		}

		return resposta;
	}

	// Request for: http://localhost:8080/link/add
	@PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> inserirLink(@RequestBody @Valid Link link, BindingResult resultado) {
		ResponseEntity<Object> resposta = null;
		try {
			if (resultado.hasErrors()) {
				resposta = new ResponseEntity<Object>(
						new Mensagem("erro", "Os dados não foram informados corretamente!", null),
						HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				link = linkService.salvarLink(link);
				if ((link != null) && (link.getId() > 0)) {
					resposta = new ResponseEntity<Object>(link, HttpStatus.OK);
				} else {
					resposta = new ResponseEntity<Object>(
							new Mensagem("erro", "não foi possível inserir o link informado no banco de dados", null),
							HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		} catch (Exception e) {
			resposta = new ResponseEntity<Object>(
					new Mensagem("erro", "não foi possível inserir o link informado no banco de dados", e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return resposta;
	}

	// Request for: http://localhost:8080/link/addLinks
	@PostMapping(value = "/addLinks", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> inserirLinks(@RequestBody @Valid Iterable<Link> links, BindingResult resultado) {
		ResponseEntity<Object> resposta = null;
		try {
			if (resultado.hasErrors()) {
				resposta = new ResponseEntity<Object>(
						new Mensagem("erro", "Os dados não foram informados corretamente!", null),
						HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				Iterable<Link> l = linkService.salvarLinks(links);
				if (l != null) {
					resposta = new ResponseEntity<Object>(l, HttpStatus.OK);
				} else {
					resposta = new ResponseEntity<Object>(
							new Mensagem("erro", "não foi possível inserir o link informado no banco de dados", null),
							HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		} catch (Exception e) {
			resposta = new ResponseEntity<Object>(
					new Mensagem("erro", "não foi possível inserir o link informado no banco de dados", e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return resposta;
	}

	// Request for: http://localhost:8080/link/update
	@PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> updateLink(@RequestBody @Valid Link link, BindingResult resultado) {
		ResponseEntity<Object> resposta = null;
		try {
			if (resultado.hasErrors()) {
				resposta = new ResponseEntity<Object>(
						new Mensagem("erro", "Os dados não foram informados corretamente!", null),
						HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				link = linkService.salvarLink(link);
				if ((link != null) && (link.getId() > 0)) {
					resposta = new ResponseEntity<Object>(link, HttpStatus.OK);
				} else {
					resposta = new ResponseEntity<Object>(
							new Mensagem("erro", "não foi possível inserir o link informado no banco de dados", null),
							HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		} catch (Exception e) {
			resposta = new ResponseEntity<Object>(
					new Mensagem("erro", "não foi possível inserir o link informado no banco de dados", e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return resposta;
	}

	// Request for: http://localhost:8080/link/encontrar/{url}
	@GetMapping(value = "/encontrar/{url}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> encontrarLink(@PathVariable(value = "url") String url) {
		ResponseEntity<Object> resposta = null;
		List<Link> links = linkService.encontrarLinkUrl(url);
		if (!links.isEmpty()) {
			resposta = new ResponseEntity<Object>(links, HttpStatus.OK);
		} else {
			resposta = new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		}
		return resposta;
	}

	// Request for: http://localhost:8080/link/ordemAlfabetica
	@GetMapping(value = "/ordemAlfabetica", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> listarEmOrdemAlfabetica() {
		ResponseEntity<Object> resposta = null;
		List<Link> links = linkService.listarEmOrdemAlfabetica();
		if (!links.isEmpty()) {
			resposta = new ResponseEntity<Object>(links, HttpStatus.OK);
		} else {
			resposta = new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		}
		return resposta;
	}

	// Request for: http://localhost:8080/link/pagina
	@GetMapping(value = "/pagina", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listarPagina() {
		ResponseEntity<Object> resposta = null;
		String links = linkService.buscarPagina();
		if (!links.isEmpty()) {
			resposta = new ResponseEntity(links, HttpStatus.OK);
		} else {
			resposta = new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		return resposta;
	}

	// Request for: http://localhost:8080/link/retornarPagina
	@GetMapping(value = "/retornarPagina", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> retornarPagina() {
		ResponseEntity<Object> resposta = null;
		List<ResultadoPagina> resultado = linkService.retornaPaginas();
		if (!resultado.isEmpty()) {
			resposta = new ResponseEntity(resultado, HttpStatus.OK);
		} else {
			resposta = new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		return resposta;
	}

	// Request for: http://localhost:8080/link/getPagina/{pag}
	@GetMapping(value = "/getPagina/{pag}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> getPagina(@PathVariable(value = "pag") int pag) {
		ResponseEntity<Object> resposta = null;
		ResultadoPagina resultado = linkService.getPag(pag);
		if (resultado != null) {
			resposta = new ResponseEntity(resultado, HttpStatus.OK);
		} else {
			resposta = new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		return resposta;
	}
}
