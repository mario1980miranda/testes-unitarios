package br.ce.wcaquino.matchers;

import java.util.Calendar;

public class OwnMatchers {

	public static DiaDaSemanaMatcher caiEm(Integer diaSemana) {
		return new DiaDaSemanaMatcher(diaSemana);
	}
	
	public static DiaDaSemanaMatcher caiNumaSegundaFeira() {
		return new DiaDaSemanaMatcher(Calendar.MONDAY);
	}
	
	public static DataDiferencaEntreDiasMatcher ehHoje() {
		return new DataDiferencaEntreDiasMatcher(0);
	}
	
	public static DataDiferencaEntreDiasMatcher ehHojeComDiferencaEmDias(Integer quantidadeDias) {
		return new DataDiferencaEntreDiasMatcher(quantidadeDias);
	}
}
