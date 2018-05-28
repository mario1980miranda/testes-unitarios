package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Usuario;

public class AssertionsTest {

	@Test
	public void test() {
		Assert.assertTrue(Boolean.TRUE);
		Assert.assertFalse(Boolean.FALSE);
		
		Assert.assertEquals(1, 1);
		
		Assert.assertEquals(0.51234, 0.5123, 0.0001); // add delta of comparison between
		Assert.assertEquals(Math.PI, 3.14, 0.01); // it passes the test becouse of the delta defined
		
		int i1 = 5;
		Integer i2 = 5;
		// box/unbox does not exist in junit, so we need to convert
		Assert.assertEquals(Integer.valueOf(i1), i2);
		Assert.assertEquals(i1, i2.intValue());
		
		Assert.assertEquals("bola", "bola");
		Assert.assertNotEquals("bola", "bala");
		Assert.assertTrue("bola".equalsIgnoreCase("Bola"));
		Assert.assertTrue("bola".startsWith("bo"));
		
		Usuario u1 = new Usuario("Usuario 1");
		Usuario u2 = new Usuario("Usuario 1");
		Usuario u3 = u2;
		
		Assert.assertEquals(u1, u2);
		
		Assert.assertNotSame(u1, u2);
		Assert.assertSame(u3, u2);
		
		u3 = null;
		Assert.assertNull(u3);
		Assert.assertNotNull(u2);
	}
}
