package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;

@FixMethodOrder	
public class OrderedTest {

	public static int contador = 0;
	
	@Test
	@Ignore
	public void inicio() {
		contador = 1;
	}
	
	@Test
	@Ignore
	public void verifica() {
		Assert.assertEquals(1, contador);
	}
}
