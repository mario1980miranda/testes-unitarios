package br.ce.wcaquino.builders;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;

import java.util.Arrays;
import java.util.Date;

import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoBuilder {

	private Locacao locacao;
	
	private LocacaoBuilder(){}
	
	public static LocacaoBuilder umaLocacao() {
		LocacaoBuilder builder = new LocacaoBuilder();
		inicializaLocacaoPadrao(builder);
		return builder;
	}

	private static void inicializaLocacaoPadrao(LocacaoBuilder builder) {
		builder.locacao = new Locacao();
		builder.locacao.setUsuario(umUsuario().agora());
		builder.locacao.setFilmes(Arrays.asList(umFilme().agora()));
		builder.locacao.setDataLocacao(new Date());
		builder.locacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(1));
		builder.locacao.setValor(4.0);
	}
	
	public Locacao agora() {
		return locacao;
	}
}
