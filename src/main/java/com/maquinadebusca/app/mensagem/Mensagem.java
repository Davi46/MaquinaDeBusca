package com.maquinadebusca.app.mensagem;

public class Mensagem {
	
	private String tipo;
	
	private String text;
	
	private String excecao;
	 
	public Mensagem() {
		
	} 

	public Mensagem(String tipo, String text, String excecao) {
		super();
		this.tipo = tipo;
		this.text = text;
		this.excecao = excecao;
	} 

	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	public String getExcecao() {
		return excecao;
	}

	public void setExcecao(String excecao) {
		this.excecao = excecao;
	}
}
