package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, Collection<Filme> filmes) throws LocadoraException, FilmeSemEstoqueException {
		
		if (filmes == null || filmes.isEmpty()) {
			throw new LocadoraException("Filme vazio");
		}
		
		if (usuario == null) {
			throw new LocadoraException("Usuario vazio");
		}
		
		List<Filme> lstFilmes = new ArrayList<Filme>(filmes);
		
		Double valorTotal = 0.0;
		
		for (int i = 0; i < lstFilmes.size(); i++) {
			
			Filme filme = lstFilmes.get(i);
			
			if (filme.getEstoque() == 0) {
				throw new FilmeSemEstoqueException();
			}
			
			Double valorFilme = filme.getPrecoLocacao();
			
			switch (i) {
			case 2:
				valorFilme = valorFilme * 0.75;
				break;
			case 3:
				valorFilme = valorFilme * 0.50;
				break;
			case 4:
				valorFilme = valorFilme * 0.25;
				break;
			case 5:
				valorFilme = valorFilme * 0.0;
				break;

			default:
				break;
			}
			
			valorTotal += valorFilme;
		}
		
		if (valorTotal == 0.0) {
			throw new LocadoraException("Valor vazio");
		}
		
		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		locacao.setValor(valorTotal);

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		if (DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
			dataEntrega = adicionarDias(dataEntrega, 1);
		}
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		
		return locacao;
	}

	public static void main(String[] args) {
		// cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Mario Miranda");
		Filme filmeA = new Filme("Filme 1", 2, 5.0);
		Filme filmeB = new Filme("Filme 2", 1, 4.0);
		Filme filmeC = new Filme("Filme 3", 5, 10.0);
		
		Collection<Filme> filmes = new ArrayList<Filme>(Arrays.asList(filmeA, filmeB, filmeC));
		
		try {
			// acao
			Locacao locacao = service.alugarFilme(usuario, filmes);
			
			// validacao
			System.out.println(locacao.getValor() == 19.0);
			System.out.println(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
			System.out.println(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}