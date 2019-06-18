package com.maquinadebusca.app.security;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.maquinadebusca.app.model.PermissaoEnum;
import com.maquinadebusca.app.model.Usuario;
import com.maquinadebusca.app.model.repository.UsuarioRepository;
import com.maquinadebusca.app.model.service.TokenAuthenticationService;

import io.jsonwebtoken.Jwts;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class JWTAuthenticationFilter extends GenericFilterBean {

	private static final String HEADER_STRING = "Authorization";
	private static final String SECRET = "MySecreteApp";
	private static final String TOKEN_PREFIX = "Bearer";

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest re = (HttpServletRequest) request;
		String token = re.getHeader(HEADER_STRING);
		//String user = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody()
				//.getSubject();
		Authentication authentication = TokenAuthenticationService.getAuthentication((HttpServletRequest) request);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		request.setAttribute("Non-Authoritative Information", "Usuário não tem permissão para acessar o método!");

		filterChain.doFilter(request, response);
	}

}
