package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {
	
	private static LocacaoService service;
	
	@Rule public ErrorCollector error = new ErrorCollector();
	
	@Rule public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setup() {
		
	}
	
	@After
	public void tearDown() {
		
	}
	
	@BeforeClass
	public static void setupClass() {
		service = new LocacaoService();
	}
	
	@AfterClass
	public static void tearDownClass() {
		
	}
	
	@Test
	public void deveAlugarFilme() throws Exception {
		
//		Assume.assume
		// cenario
		Usuario usuario = new Usuario("Mario Miranda");
		Filme filmeA = new Filme("Filme 1", 2, 5.0);
		
		Collection<Filme> filmes = new ArrayList<Filme>(Arrays.asList(filmeA));
		
		// acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		// verificacao
		Assert.assertEquals(5.0, locacao.getValor(), 0.01);
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
		
		// We can use fluent interface and static import to make reading easier
		assertThat(locacao.getValor(), is(equalTo(5.0)));
		assertThat(locacao.getValor(), is(not(6.0)));
		assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
		
		// with ErrorCollector tests continue after finding the first error
//		error.checkThat(locacao.getValor(), is(equalTo(6.0)));
//		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
//		error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(false));
	}
	
	@Test(expected=FilmeSemEstoqueException.class) 
	/**
	 * Forma elegante: Simples, enxuta, porem muito superficial
	 * Uma anotacao e adicionada para que o Junit capture-a e trate-a
	 * Funciona bem quando apenas a exception importa para o test.
	 */
	public void naoDeveAlugarFilmeSemEstoque_Elegante() throws Exception {
		Usuario usuario = new Usuario("Mario Miranda");
		Filme filmeA = new Filme("Filme 1", 2, 5.0);
		Filme filmeB = new Filme("Filme 2", 0, 4.0);
		Filme filmeC = new Filme("Filme 3", 5, 10.0);
		
		Collection<Filme> filmes = new ArrayList<Filme>(Arrays.asList(filmeA, filmeB, filmeC));

		service.alugarFilme(usuario, filmes);
	}
	
	@Test 
	/**
	 * Forma Robusta: maior controle sobre a execucao do teste, inclusive pode continuar o codigo apos o catch
	 * Nao e possivel capturar a mensagem da axception para que se possa garantir
	 * O desenvolvedor deve tratar todas as excecoes explicitamente, nao deixando para o Junit tratar a exception
	 * Solucao mais completa
	 */
	public void naoDeveAlugarFilmeSemEstoque_Robusta() {
		Usuario usuario = new Usuario("Mario Miranda");
		Filme filmeA = new Filme("Filme 1", 2, 5.0);
		Filme filmeB = new Filme("Filme 2", 1, 4.0);
		Filme filmeC = new Filme("Filme 3", 0, 10.0);
		
		Collection<Filme> filmes = new ArrayList<Filme>(Arrays.asList(filmeA, filmeB, filmeC));
		
		try {
			service.alugarFilme(usuario, filmes);
			Assert.fail("Deveria ter lançado uma exceção");
		} catch (Exception e) {
//			e.printStackTrace();
			Assert.assertThat(e.getMessage(), is("Filme sem estoque"));
		}
	}
	
	@Test
	/**
	 * Permite configurar que tipo de excpetion sera capturada e inclusive comparar a mensagem
	 * Uma regra de exception deve ser declarada no inicio da classe: @ExpectedException
	 * A regra deve ser declarada antes da acao (de chamar a classe de servico)
	 */
	public void naoDeveAlugarFilmeSemEstoque_RegraExcecaoEsperada() throws Exception {
		Usuario usuario = new Usuario("Mario Miranda");
		Filme filmeA = new Filme("Filme 1", 0, 5.0);
		Filme filmeB = new Filme("Filme 2", 1, 4.0);
		Filme filmeC = new Filme("Filme 3", 5, 10.0);
		
		Collection<Filme> filmes = new ArrayList<Filme>(Arrays.asList(filmeA, filmeB, filmeC));

		exception.expect(Exception.class);
		exception.expectMessage("Filme sem estoque");
		
		service.alugarFilme(usuario, filmes);
	}
	
	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
		Filme filmeA = new Filme("Filme 1", 2, 5.0);
		Filme filmeB = new Filme("Filme 2", 1, 4.0);
		Filme filmeC = new Filme("Filme 3", 5, 10.0);
		
		Collection<Filme> filmes = new ArrayList<Filme>(Arrays.asList(filmeA, filmeB, filmeC));

		try {
			service.alugarFilme(null, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Usuario vazio"));
		}
	}
	
	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {
		Usuario usuario = new Usuario("Mario Miranda");

		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");
		
		service.alugarFilme(usuario, null);
	}
	
//	@Test
//	public void devePagar75PctNoFilme3() throws LocadoraException, FilmeSemEstoqueException {
//		//cenario
//		Usuario usuario = new Usuario("User 1");
//		Collection<Filme> filmes = new ArrayList<Filme>(Arrays.asList(new Filme("Filme 1", 2, 4.0),
//				new Filme("Filme 2", 2, 4.0), new Filme("Filme 3", 2, 4.0)));
//		//acao
//		Locacao resultado = service.alugarFilme(usuario, filmes);
//		
//		//verificacao
//		//4+4+3=11
//		assertThat(resultado.getValor(), is(11.0));
//	}
//	
//	@Test
//	public void devePagar50PctNoFilme4() throws LocadoraException, FilmeSemEstoqueException {
//		//cenario
//		Usuario usuario = new Usuario("User 1");
//		Collection<Filme> filmes = new ArrayList<Filme>(Arrays.asList(new Filme("Filme 1", 2, 4.0),
//				new Filme("Filme 2", 2, 4.0), new Filme("Filme 3", 2, 4.0), new Filme("Filme 4", 2, 4.0)));
//		//acao
//		Locacao resultado = service.alugarFilme(usuario, filmes);
//		
//		//verificacao
//		//4+4+3+2=13
//		assertThat(resultado.getValor(), is(13.0));
//	}
//	
//	@Test
//	public void devePagar25PctNoFilme5() throws LocadoraException, FilmeSemEstoqueException {
//		//cenario
//		Usuario usuario = new Usuario("User 1");
//		Collection<Filme> filmes = new ArrayList<Filme>(
//				Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0), new Filme("Filme 3", 2, 4.0),
//						new Filme("Filme 4", 2, 4.0), new Filme("Filme 5", 2, 4.0)));
//		//acao
//		Locacao resultado = service.alugarFilme(usuario, filmes);
//		
//		//verificacao
//		//4+4+3+2+1=14
//		assertThat(resultado.getValor(), is(14.0));
//	}
//	
//	@Test
//	public void devePagar0PctNoFilme6() throws LocadoraException, FilmeSemEstoqueException {
//		//cenario
//		Usuario usuario = new Usuario("User 1");
//		Collection<Filme> filmes = new ArrayList<Filme>(
//				Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0), new Filme("Filme 3", 2, 4.0),
//						new Filme("Filme 4", 2, 4.0), new Filme("Filme 5", 2, 4.0), new Filme("Filme 6", 2, 4.0)));
//		//acao
//		Locacao resultado = service.alugarFilme(usuario, filmes);
//		
//		//verificacao
//		//4+4+3+2+1+0=14
//		assertThat(resultado.getValor(), is(14.0));
//	}
	
	@Test
	@Ignore
	public void naoDeveDevolverFilmeNoDomingo() throws LocadoraException, FilmeSemEstoqueException {
		Usuario usuario = new Usuario("Cristina");
		Collection<Filme> filmes = Arrays.asList(new Filme("Filme1", 1, 5.0));
		
		Locacao retorno = service.alugarFilme(usuario, filmes);
		
		boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
		Assert.assertTrue(ehSegunda);
	}
}