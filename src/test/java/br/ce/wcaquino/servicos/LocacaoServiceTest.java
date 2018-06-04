package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.builders.LocacaoBuilder.umaLocacao;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.OwnMatchers.caiNumaSegundaFeira;
import static br.ce.wcaquino.matchers.OwnMatchers.ehHoje;
import static br.ce.wcaquino.matchers.OwnMatchers.ehHojeComDiferencaEmDias;
import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterData;
import static java.lang.Boolean.TRUE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocacaoService.class, DataUtils.class})
public class LocacaoServiceTest {
	
	@InjectMocks
	private LocacaoService service;
	
	@Mock
	private SPCService spcService;
	@Mock
	private EmailService emailService;
	@Mock
	private LocacaoDAO dao;
	
	@Rule public ErrorCollector error = new ErrorCollector();
	
	@Rule public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
//		service = new LocacaoService();
//		dao = Mockito.mock(LocacaoDAO.class);
//		service.setLocacaoDAO(dao);
//		spcService = Mockito.mock(SPCService.class);
//		service.setSPCService(spcService);
//		emailService = Mockito.mock(EmailService.class);
//		service.setEmailService(emailService);
	}
	
	@After
	public void tearDown() {
		
	}
	
	@BeforeClass
	public static void setupClass() {

	}
	
	@AfterClass
	public static void tearDownClass() {
		
	}
	
	@Test
	public void deveAlugarFilme() throws Exception {
		
		// cenario
		Usuario usuario = umUsuario().agora();
		Filme filmeA = umFilme().comValor(5.0).agora();
		PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(28, 4, 2017));
		
		Collection<Filme> filmes = new ArrayList<Filme>(Arrays.asList(filmeA));
		
		// acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		// verificacao
//		Assert.assertEquals(5.0, locacao.getValor(), 0.01);
//		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
//		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
		
		// We can use fluent interface and static import to make reading easier
//		assertThat(locacao.getValor(), is(equalTo(5.0)));
//		assertThat(locacao.getValor(), is(not(6.0)));
//		assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
//		assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
		
		// with ErrorCollector tests continue after finding the first error
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
//		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(locacao.getDataLocacao(), ehHoje());
		error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaEmDias(1));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), obterData(28, 4, 2017)) , is(TRUE));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), obterData(29, 4, 2017)) , is(TRUE));
	}
	
	@Test(expected=FilmeSemEstoqueException.class) 
	/**
	 * Forma elegante: Simples, enxuta, porem muito superficial
	 * Uma anotacao e adicionada para que o Junit capture-a e trate-a
	 * Funciona bem quando apenas a exception importa para o test.
	 */
	public void naoDeveAlugarFilmeSemEstoque_Elegante() throws Exception {
		Usuario usuario = umUsuario().agora();
		Filme filmeA = umFilme().agora();
		Filme filmeB = umFilmeSemEstoque().agora();
		Filme filmeC = umFilme().agora();
		
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
		Usuario usuario = umUsuario().agora();
		Filme filmeA = umFilme().semEstoque().agora();
		Filme filmeB = umFilme().agora();
		Filme filmeC = umFilme().agora();
		
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
		Usuario usuario = umUsuario().agora();
		Filme filmeA = umFilme().agora();
		Filme filmeB = umFilme().agora();
		Filme filmeC = umFilme().semEstoque().agora();
		
		Collection<Filme> filmes = new ArrayList<Filme>(Arrays.asList(filmeA, filmeB, filmeC));

		exception.expect(Exception.class);
		exception.expectMessage("Filme sem estoque");
		
		service.alugarFilme(usuario, filmes);
	}
	
	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
		Filme filmeA = umFilme().agora();
		Filme filmeB = umFilme().agora();
		Filme filmeC = umFilme().agora();
		
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
		Usuario usuario = umUsuario().agora();

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
	public void naoDeveDevolverFilmeNoDomingo() throws Exception {
		
		// cenario
		Usuario usuario = umUsuario().agora();
		Collection<Filme> filmes = Arrays.asList(umFilme().agora());
		
		PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(29, 4, 2017));
		
		// acao
		Locacao retorno = service.alugarFilme(usuario, filmes);
		
		// verificacao
		
//		boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
//		Assert.assertTrue(ehSegunda);
//		assertThat(retorno.getDataLocacao(), caiEm(Calendar.MONDAY));
//		assertThat(retorno.getDataLocacao(), caiNumaSegundaFeira());
		
//		assertThat(retorno.getDataLocacao(), new DiaDaSemanaMatcher(Calendar.MONDAY));
		
//		assertThat(retorno.getDataLocacao(), caiEm(Calendar.MONDAY));
		assertThat(retorno.getDataRetorno(), caiNumaSegundaFeira());
	}
	
	@Test
	public void naoDeveAlugarFilmeParaUsuarioNegativadoSPC() throws Exception {
		// cenario
		Usuario usuario = umUsuario().agora();
//		Usuario usuario2 = umUsuario().comNome("Usuario 2").agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
//		when(spcService.possuiNegativacao(usuario)).thenReturn(TRUE);
		when(spcService.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(TRUE);
		
		// acao
		try {
			service.alugarFilme(usuario, filmes);
			
		// verificacao
			Assert.fail(); // para nao gerar um falso positivo!!!
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Usuario negativado"));
		}
		
		verify(spcService).possuiNegativacao(usuario);
	}
	
	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() {
		// cenario
		Usuario usuario1 = umUsuario().agora();
		Usuario usuario2 = umUsuario().comNome("Usuario Em Dia").agora();
		Usuario usuario3 = umUsuario().comNome("Usuario Em Atraso").agora();
		List<Locacao> locacoes = Arrays.asList(
				umaLocacao().comUsuario(usuario1).comDataEntregaAtrasada().agora(),
				umaLocacao().comUsuario(usuario2).agora(),
				umaLocacao().comUsuario(usuario3).comDataEntregaAtrasada().agora(),
				umaLocacao().comUsuario(usuario3).comDataEntregaAtrasada().agora()
				);
		
		when(dao.obterLocacoesPendentes()).thenReturn(locacoes);
		
		// acao
		service.notificarAtrasos();
		
		// verificacao
		verify(emailService, times(3)).notificarAtraso(Mockito.any(Usuario.class));
		verify(emailService).notificarAtraso(usuario1);
		verify(emailService, never()).notificarAtraso(usuario2);
		verify(emailService, times(2)).notificarAtraso(usuario3);
		verify(emailService, atLeast(2)).notificarAtraso(usuario3);
		verify(emailService, atMost(2)).notificarAtraso(usuario3);
		verify(emailService, atLeastOnce()).notificarAtraso(usuario3);
		verifyNoMoreInteractions(emailService);
		verifyZeroInteractions(spcService); // apenas para conhecimento, metodo nao tem interacao
	}
	
	@Test
	public void deveTratarErroAoObterFalhaAoConsultarSPC() throws Exception {
		// cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		when(spcService.possuiNegativacao(usuario)).thenThrow(new Exception("Falha catastrofica"));

		// verificacao
		exception.expect(LocadoraException.class);
		exception.expectMessage("Problemas com SPC, tente novamente");
		
		// acao
		service.alugarFilme(usuario, filmes);
	}
	
	@Test
	public void deveProrrogarUmaLocacao() {
		// cenario
		Locacao locacao = umaLocacao().agora();
		
		// acao
		service.prorrogarLocacao(locacao, 3);
		
		// verificacao
		ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
		Mockito.verify(dao).salvar(argCapt.capture());
		Locacao novaLocacao = argCapt.getValue();
		
		error.checkThat(novaLocacao.getValor(), is(12.0));
		error.checkThat(novaLocacao.getDataLocacao(), ehHoje());
		error.checkThat(novaLocacao.getDataRetorno(), ehHojeComDiferencaEmDias(3));
	}
}