package br.ce.wcaquino.entidades;

import java.util.Collection;
import java.util.Date;

public class Locacao {

	private Usuario usuario;
	private Collection<Filme> filmes;
	private Date dataLocacao;
	private Date dataRetorno;
	private Double valor;
	
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Date getDataLocacao() {
		return dataLocacao;
	}
	public void setDataLocacao(Date dataLocacao) {
		this.dataLocacao = dataLocacao;
	}
	public Date getDataRetorno() {
		return dataRetorno;
	}
	public void setDataRetorno(Date dataRetorno) {
		this.dataRetorno = dataRetorno;
	}
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	public Collection<Filme> getFilmes() {
		return filmes;
	}
	public void setFilmes(Collection<Filme> filmes) {
		this.filmes = filmes;
	}
}