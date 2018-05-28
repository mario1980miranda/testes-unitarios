package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Assert;
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
	
	@Rule public ErrorCollector error = new ErrorCollector();
	
	@Rule public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void testeLocacao() throws Exception {
		// cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Mario Miranda");
		Filme filme = new Filme("Filme 1", 2, 5.0);
		
		// acao
		Locacao locacao = service.alugarFilme(usuario, filme);
		
		// validacao
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
	public void testeLocacaoFilmeSemEstoque_Elegante() throws Exception {
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Mario Miranda");
		Filme filme = new Filme("Filme 1", 0, 5.0);

		service.alugarFilme(usuario, filme);
	}
	
	@Test 
	/**
	 * Forma Robusta: maior controle sobre a execucao do teste, inclusive pode continuar o codigo apos o catch
	 * Nao e possivel capturar a mensagem da axception para que se possa garantir
	 * O desenvolvedor deve tratar todas as excecoes explicitamente, nao deixando para o Junit tratar a exception
	 * Solucao mais completa
	 */
	public void testeLocacaoFilmeSemEstoque_Robusta() {
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Mario Miranda");
		Filme filme = new Filme("Filme 1", 0, 5.0);
		
		try {
			service.alugarFilme(usuario, filme);
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
	public void testeLocacaoFilmeSemEstoque_RegraExcecaoEsperada() throws Exception {
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Mario Miranda");
		Filme filme = new Filme("Filme 1", 0, 5.0);

		exception.expect(Exception.class);
		exception.expectMessage("Filme sem estoque");
		
		service.alugarFilme(usuario, filme);
	}
	
	@Test
	public void usuarioVazio() throws FilmeSemEstoqueException {
		LocacaoService service = new LocacaoService();
		Filme filme = new Filme("Filme 1", 2, 5.0);

		try {
			service.alugarFilme(null, filme);
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Usuario vazio"));
		}
	}
	
	@Test
	public void filmeVazio() throws FilmeSemEstoqueException, LocadoraException {
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Mario Miranda");

		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");
		
		service.alugarFilme(usuario, null);
	}
}