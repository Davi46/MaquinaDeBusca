package com.maquinadebusca.app.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.maquinadebusca.app.model.service.UsuarioService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable().authorizeRequests()
			//.antMatchers("/").permitAll() 
			.antMatchers(HttpMethod.GET).permitAll()
			.antMatchers(HttpMethod.POST).permitAll()
			//.antMatchers(HttpMethod.PUT).permitAll()
			.antMatchers(HttpMethod.OPTIONS).permitAll()
			//.antMatchers(HttpMethod.POST, "/login").permitAll()
			//.antMatchers(HttpMethod.POST, "/user").permitAll() 
			.anyRequest().authenticated()
			.and();
			
			// filtra requisições de login
			//.addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class);
			//.addFilterBefore(new JWTLoginFilter("/user", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
			
			// filtra outras requisições para verificar a presença do JWT no header
			//.addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	 
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// cria uma conta default
		/*auth.inMemoryAuthentication()
			.withUser("admin")
			.password("{noop}password")
			.roles("ADMIN");*/
		bCryptPasswordEncoder = new BCryptPasswordEncoder();
		auth.userDetailsService(usuarioService).passwordEncoder(bCryptPasswordEncoder);  
		 
	}
}
