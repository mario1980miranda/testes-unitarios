package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
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
import org.mockito.Mockito;

import br.ce.wcaquino.daos.LocacaoDAO;
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
	
	private SPCService spcService;
	
	private LocacaoDAO dao;
	
	private static Filme filmeA = umFilme().agora();
	private static Filme filmeB = umFilme().agora();
	private static Filme filmeC = umFilme().agora();
	private static Filme filmeD = umFilme().agora();
	private static Filme filmeE = umFilme().agora();
	private static Filme filmeF = umFilme().agora();
	private static Filme filmeG = umFilme().agora();
	
	@Before
	public void setup() {
		service = new LocacaoService();
		dao = Mockito.mock(LocacaoDAO.class);
		service.setLocacaoDAO(dao);
		spcService = Mockito.mock(SPCService.class);
		service.setSPCService(spcService);
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
		Usuario usuario = umUsuario().agora();
		
		//acao
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//verificacao
		//4+4+3+2+1+0=14
		assertThat(resultado.getValor(), is(valorLocacao));
	}
}
