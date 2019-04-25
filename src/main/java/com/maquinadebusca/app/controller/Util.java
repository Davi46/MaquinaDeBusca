package com.maquinadebusca.app.controller;

import io.jsonwebtoken.Jwts;

public class Util {

	private static final String SECRET = "MySecreteApp";
	private static final String TOKEN_PREFIX = "Bearer";
	private static final String HEADER_STRING = "Authorization";

	public static String getUser(String token) {
		return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody()
				.getSubject();
	}
}
