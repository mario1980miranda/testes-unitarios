package br.ce.wcaquino.servicos;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTeste {
	
	@Parameter(value=0)
	public List<Filme> filmes;
	
	@Parameter(value=1)
	public Double valorLocacao;
	
	@Parameter(value=2)
	public String cenario;
	
	private LocacaoService service;
	
	private static Filme filmeA = new Filme("Filme 1", 2, 4.0);
	private static Filme filmeB = new Filme("Filme 2", 5, 4.0);
	private static Filme filmeC = new Filme("Filme 3", 3, 4.0);
	private static Filme filmeD = new Filme("Filme 4", 1, 4.0);
	private static Filme filmeE = new Filme("Filme 5", 8, 4.0);
	private static Filme filmeF = new Filme("Filme 6", 7, 4.0);
	private static Filme filmeG = new Filme("Filme 7", 1, 4.0);
	
	@Before
	public void setup() {
		service = new LocacaoService();
	}
	
	@Parameters(name = "{2}")
	public static Collection<Object[]> getParametros() {
		return Arrays.asList(new Object[][] {
			{Arrays.asList(filmeA,filmeB), 8.0, "deveConcederDescontoDe0PctAoAlugar2Filmes"},
			{Arrays.asList(filmeA,filmeB,filmeC), 11.0, "deveConcederDescontoDe25PctAoAlugar3Filmes"},
			{Arrays.asList(filmeA,filmeB,filmeC,filmeD), 13.0, "deveConcederDescontoDe50PctAoAlugar4Filmes"},
			{Arrays.asList(filmeA,filmeB,filmeC,filmeD,filmeE), 14.0, "deveConcederDescontoDe75PctAoAlugar5Filmes"},
			{Arrays.asList(filmeA,filmeB,filmeC,filmeD,filmeE,filmeF), 14.0, "deveConcederDescontoDe100PctAoAlugar6Filmes"},
			{Arrays.asList(filmeA,filmeB,filmeC,filmeD,filmeE,filmeF,filmeG), 18.0, "deveConcederDescontoDe0PctNoUltimoFilmeAoAlugar7Filmes"}
		});
	}
	
	@Test
	public void deveCalcularValorLocacaoConsiderandoDescontos() throws LocadoraException, FilmeSemEstoqueException {
		//cenario
		Usuario usuario = new Usuario("User 1");
		
		//acao
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//verificacao
		//4+4+3+2+1+0=14
		assertThat(resultado.getValor(), is(valorLocacao));
	}
}
