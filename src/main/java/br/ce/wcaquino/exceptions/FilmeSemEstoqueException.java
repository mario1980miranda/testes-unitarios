package br.ce.wcaquino.exceptions;

public class FilmeSemEstoqueException extends Exception {

	public FilmeSemEstoqueException() {
		super("Filme sem estoque");
	}

	private static final long serialVersionUID = 4617296203742175210L;

}
