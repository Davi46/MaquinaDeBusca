package com.maquinadebusca.app.model.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.maquinadebusca.app.model.MyUserPrincipal;
import com.maquinadebusca.app.model.PermissaoEnum;
import com.maquinadebusca.app.model.Usuario;
import com.maquinadebusca.app.model.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UsuarioRepository ur;

	public UsuarioService() {
		bCryptPasswordEncoder = new BCryptPasswordEncoder(); 
	}

	public Usuario saveUsuario(Usuario usuario) {
		usuario.setSenha(bCryptPasswordEncoder.encode(usuario.getSenha()));
		return ur.save(usuario);
	}

	public List<Usuario> getUsuarios() {
		Iterable<Usuario> usuarios = ur.findAll();
		List<Usuario> resposta = new ArrayList<Usuario>();
		for (Usuario usuario : usuarios) {
			resposta.add(usuario);
		}
		return resposta;
	}

	public Usuario getUsuarioById(long id) {
		Usuario usuario = ur.findById(id);
		return usuario;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { 
		for (Usuario usuario : ur.findAll()) {
			if (usuario.getUsuario().equals(username)) {
				return new MyUserPrincipal(usuario);
			}
		}
		throw new UsernameNotFoundException(username); 
	}
	
	public Usuario getUsuarioByUser(String user) { 
		for (Usuario usuario : ur.findAll()) {
			if (usuario.getUsuario().equals(user)) {
				return usuario;
			}
		}
		
		return null;
	}

	public Boolean verificaPermissao(String user, HttpMethod metodo) {  
		Usuario usuario = getUsuarioByUser(user);
		if(!metodo.equals(HttpMethod.GET) && !usuario.getPermissao().equals(PermissaoEnum.Permissao.ADMIN.getCode())) {
			return false;
		}
		
		return true;
	}

}
