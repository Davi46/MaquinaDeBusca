package com.maquinadebusca.app.controller; 
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maquinadebusca.app.model.Host;
import com.maquinadebusca.app.model.service.HostService; 
  
@RestController
@RequestMapping("/host") // URL: http://localhost:8080/host
public class HostController {
	@Autowired
	HostService hostService;

	// URL: http://localhost:8080/host/listar
	@GetMapping(value = "/listar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listar() {
		ResponseEntity<Object> resposta = null;
		List<Host> hosts = hostService.getHosts();
		if (!hosts.isEmpty()) {
			resposta = new ResponseEntity<Object>(hosts, HttpStatus.OK);
		} else {
			resposta = new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		}
		return resposta;
	}

	// Request for: http://localhost:8080/host/listar/{id}
	@GetMapping(value = "/listar/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listar(@PathVariable(value = "id") long id) {
		ResponseEntity<Object> resposta = null;
		Host hosts = hostService.getHostById(id);
		if (hosts != null) {
			resposta = new ResponseEntity<Object>(hosts, HttpStatus.OK);
		} else {
			resposta = new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		}
		return resposta; 
	}

	// Request for: http://localhost:8080/host/listar/{url}
	@GetMapping(value = "/listar/{host}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listar(@PathVariable(value = "host") String host) {
		ResponseEntity<Object> resposta = null;
		Host hosts = hostService.getByHost(host);
		if (hosts != null) {
			resposta = new ResponseEntity<Object>(hosts, HttpStatus.OK);
		} else {
			resposta = new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		}
		return resposta;  
	} 
} 