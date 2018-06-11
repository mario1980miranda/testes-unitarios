package br.ce.wcaquino.suite;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.ce.wcaquino.servicos.AssertionsTest;
import br.ce.wcaquino.servicos.CalculoValorLocacaoTeste;
import br.ce.wcaquino.servicos.LocacaoServiceTest;

//@RunWith(Suite.class)
@SuiteClasses({ 
	CalculoValorLocacaoTeste.class, 
	LocacaoServiceTest.class, 
	AssertionsTest.class 
	})
public class SuiteExecucao {
	
	@BeforeClass
	public static void before() {
		System.out.println("Before Suite");
	}
	
	@AfterClass
	public static void after() {
		System.out.println("After Suite");
	}
}
