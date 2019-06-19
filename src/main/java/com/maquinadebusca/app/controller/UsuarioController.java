package com.maquinadebusca.app.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.maquinadebusca.app.mensagem.Mensagem;
import com.maquinadebusca.app.model.Link;
import com.maquinadebusca.app.model.Usuario;
import com.maquinadebusca.app.model.service.UsuarioService;

@RestController
@RequestMapping("/user") // URL: http://localhost:8080/user
public class UsuarioController {
	@Autowired
	UsuarioService usuarioService;

	private static final String HEADER_STRING = "Authorization"; 

	// Request for: http://localhost:8080/user/add
	@PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> inserirUser(@RequestBody @Valid Usuario user, BindingResult resultado,
			HttpServletRequest request) {
		ResponseEntity<Object> resposta = null;
		try {
			/*String token = request.getHeader(HEADER_STRING);
			if (resultado.hasErrors()) {
				resposta = new ResponseEntity<Object>(
						new Mensagem("erro", "Os dados não foram informados corretamente!", null),
						HttpStatus.INTERNAL_SERVER_ERROR);
			} else if (!usuarioService.verificaPermissao(Util.getUser(token), HttpMethod.POST)) {
				resposta = new ResponseEntity<Object>(
						new Mensagem("erro", "Usuário não tem permissão para acessar o método!", null),
						HttpStatus.NON_AUTHORITATIVE_INFORMATION);
			} else {*/
				user = usuarioService.saveUsuario(user);
				if ((user != null) && (user.getId() > 0)) {
					resposta = new ResponseEntity<Object>(user, HttpStatus.OK);
				} else {
					resposta = new ResponseEntity<Object>(new Mensagem("erro",
							"não foi possível inserir o usuario informado no banco de dados", null),
							HttpStatus.INTERNAL_SERVER_ERROR);
				}
			//}
		} catch (Exception e) {
			resposta = new ResponseEntity<Object>(new Mensagem("erro",
					"não foi possível inserir o usuario informado no banco de dados", e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return resposta;
	}

	// URL: http://localhost:8080/user/listar
	@GetMapping(value = "/listar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> listar() {
		ResponseEntity<Object> resposta = null;
		List<Usuario> users = usuarioService.getUsuarios();
		if (!users.isEmpty()) {
			resposta = new ResponseEntity<Object>(users, HttpStatus.OK);
		} else {
			resposta = new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		}
		return resposta;

	}

	// URL: http://localhost:8080/user/getUser
	@GetMapping(value = "/getUser", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity getUser(HttpServletRequest request) {
		ResponseEntity resposta = null;
		String token = request.getHeader(HEADER_STRING);
		Usuario usuario = usuarioService.getUsuarioByUser(Util.getUser(token));

		if (usuario != null) {
			resposta = new ResponseEntity<Object>(usuario, HttpStatus.OK);
		} else {
			resposta = new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		}
		return resposta;
	}

	// URL: http://localhost:8080/user/logar/{usuario}/{senha}
	@GetMapping(value = "/logar/{usuario}/{senha}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity logar(@PathVariable(value = "usuario") String usuario, @PathVariable(value = "senha") String senha) {
		ResponseEntity<Object> resposta = null;
		try {

			Usuario user = usuarioService.getUsuarioByUser(usuario);
			if ((user != null) && (user.getSenha().equals(senha))) {
				user.setSenha(null);
				resposta = new ResponseEntity<Object>(user, HttpStatus.OK);
			} else {
				resposta = new ResponseEntity<Object>(
						new Mensagem("erro", "Usuario nao encontrado ou senha incorreta", null),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {
			resposta = new ResponseEntity<Object>(
					new Mensagem("erro", "não foi possível logar no sistema", e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return resposta;
	}

}
