package com.maquinadebusca.app.controller; 

import com.maquinadebusca.app.mensagem.Mensagem;
import com.maquinadebusca.app.model.Consulta;
import com.maquinadebusca.app.model.EntradaRanking;
import com.maquinadebusca.app.model.Usuario;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.maquinadebusca.app.model.service.ProcessadorConsultaService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping ("/processador") // URL: http://localhost:8080/processador
public class ProcessadorConsultaController {

  @Autowired
  ProcessadorConsultaService pcs;

	// URL: http://localhost:8080/processador/consulta/{consultaDoUsuario}
	@GetMapping(value = "/consulta/{consultaDoUsuario}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity consultar(@PathVariable("consultaDoUsuario") String textoConsulta) {
		Consulta consulta = pcs.processarConsulta(textoConsulta);
		ResponseEntity resp;

		if (!consulta.getRanking().isEmpty()) {
			resp = new ResponseEntity(consulta, HttpStatus.OK);
		} else {
			resp = new ResponseEntity(new Mensagem("erro", "o índice invertido não pode ser criado", null),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}
	
	// URL: http://localhost:8080/processador/consultaAplicacao/{consultaAplicacao}
	@GetMapping(value = "/consultaAplicacao/{consultaAplicacao}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<EntradaRanking>> consultarAplicacao(@PathVariable("consultaAplicacao") String textoConsulta) {
		ResponseEntity resp = null;

		try {
			List<EntradaRanking> result = pcs.processarConsultaAplicacao(textoConsulta);
			if (result.size() > 0) {
				resp = new ResponseEntity<List<EntradaRanking>>(result, HttpStatus.OK);
			} else {
				resp = new ResponseEntity<Object>("Nao foram encontrado possiveis resultados", HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			resp = new ResponseEntity<Object>(
					new Mensagem("erro", "não foi possível consultar", e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return resp;
	}

}
 
