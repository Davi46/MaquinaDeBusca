package com.maquinadebusca.app.controller; 
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController; 
import com.maquinadebusca.app.model.service.HostService; 
  
@RestController
@RequestMapping("/host") // URL: http://localhost:8080/host
public class HostController {
	@Autowired
	HostService hostService;

	// URL: http://localhost:8080/host/listar
	@GetMapping(value = "/listar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listar() {
		return new ResponseEntity(hostService.getHosts(), HttpStatus.OK);
	}

	// Request for: http://localhost:8080/host/listar/{id}
	@GetMapping(value = "/listar/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listar(@PathVariable(value = "id") long id) {
		return new ResponseEntity(hostService.getHostById(id), HttpStatus.OK);
	}

	// Request for: http://localhost:8080/host/listar/{url}
	@GetMapping(value = "/listar/{host}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listar(@PathVariable(value = "host") String host) {
		return new ResponseEntity(hostService.getByHost(host), HttpStatus.OK);
	}
	 
	@GetMapping(value = "/create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity create() {
		return new ResponseEntity(hostService.create(), HttpStatus.OK);
	}
} 