package com.gvs.crm.avaliador;

public class Valor {
	public static final int CARACTER = 0;

	public static final int NUMERO = 1;

	private double dValor;

	private String sValor;

	private int tipo;

	public Valor(double dValor) {
		this.atribuirValor(dValor);
	}

	public Valor(String sValor) {
		this.atribuirValor(sValor);
	}

	public void acumular(Valor valor) {
		this.dValor = this.dValor + valor.obterValorNumerico();
	}

	public void atribuirValor(double dValor) {
		this.tipo = Valor.NUMERO;
		this.dValor = dValor;
	}

	public void atribuirValor(String sValor) {
		this.tipo = Valor.CARACTER;
		this.sValor = sValor;
	}

	public boolean diferente(Valor valor) {
		if (this.tipo == Valor.CARACTER)
			return !this.sValor.equals(valor.obterValorCaracter());
		else if (this.tipo == Valor.NUMERO)
			return this.dValor != valor.obterValorNumerico();
		return false;
	}

	public boolean igual(Valor valor) {
		if (this.tipo == Valor.CARACTER)
			return this.sValor.equals(valor.obterValorCaracter());
		else if (this.tipo == Valor.NUMERO)
			return this.dValor == valor.obterValorNumerico();
		return false;
	}

	public boolean maior(Valor valor) {
		if (this.tipo == Valor.CARACTER)
			return this.sValor.compareTo(valor.obterValorCaracter()) > 0;
		else if (this.tipo == Valor.NUMERO)
			return this.dValor > valor.obterValorNumerico();
		return false;
	}

	public boolean maiorIgual(Valor valor) {
		if (this.tipo == Valor.CARACTER)
			return this.sValor.compareTo(valor.obterValorCaracter()) >= 0;
		else if (this.tipo == Valor.NUMERO)
			return this.dValor >= valor.obterValorNumerico();
		return false;
	}

	public boolean menor(Valor valor) {
		if (this.tipo == Valor.CARACTER)
			return this.sValor.compareTo(valor.obterValorCaracter()) < 0;
		else if (this.tipo == Valor.NUMERO)
			return this.dValor < valor.obterValorNumerico();
		return false;
	}

	public boolean menorIgual(Valor valor) {
		if (this.tipo == Valor.CARACTER)
			return this.sValor.compareTo(valor.obterValorCaracter()) <= 0;
		else if (this.tipo == Valor.NUMERO)
			return this.dValor <= valor.obterValorNumerico();
		return false;
	}

	public void multiplicar(Valor valor) {
		this.dValor = this.dValor * valor.obterValorNumerico();
	}

	public int obterTipo() {
		return this.tipo;
	}

	public String obterValorCaracter() {
		return this.sValor;
	}

	public double obterValorNumerico() {
		return this.dValor;
	}

	public void subtrair(Valor valor) {
		this.dValor = this.dValor - valor.obterValorNumerico();
	}
}