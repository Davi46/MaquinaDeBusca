package com.maquinadebusca.app.controller; 

import com.maquinadebusca.app.mensagem.Mensagem;
import com.maquinadebusca.app.model.Consulta;
import com.maquinadebusca.app.model.EntradaRanking;

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
public class ProcessadorConsulta {

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
	public ResponseEntity consultarAplicacao(@PathVariable("consultaAplicacao") String textoConsulta) {
		List<EntradaRanking> result = pcs.processarConsultaAplicacao(textoConsulta);
		ResponseEntity resp;

		if (result.size() > 0) {
			resp = new ResponseEntity(result, HttpStatus.OK);
		} else {
			resp = new ResponseEntity(new Mensagem("erro", "Nao foram encontrado possiveis resultados", null),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}

}
