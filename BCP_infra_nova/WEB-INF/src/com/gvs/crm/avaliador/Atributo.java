package com.gvs.crm.avaliador;

import java.text.DecimalFormat;
import java.text.ParseException;

public class Atributo {

	private String nome;

	private Valor valor;

	public Atributo(String nome) throws Exception {
		if (nome == null || nome.length() == 0)
			throw new Exception("Nome do atributo não pode ser nulo ou vazio");
		if ("\"'".indexOf(nome.charAt(0)) >= 0) {
			this.nome = this.toString();
			this.valor = new Valor(nome.substring(1, nome.length() - 1));
		} else {
			try {
				this.nome = this.toString();
				double valorNumerico = new DecimalFormat("#,##0.00")
						.parse(nome).doubleValue();
				this.valor = new Valor(valorNumerico);
			} catch (ParseException exception) {
				this.nome = nome;
				this.valor = null;
			}
		}
	}

	public void acumularValor(Atributo atributo) {
		if (this.valor != null)
			this.valor.acumular(atributo.obterValor());
	}

	public void atribuirValor(Atributo atributo) {
		this.valor = atributo.obterValor();
	}

	public void atribuirValor(Valor valor) {
		this.valor = valor;
	}

	public boolean diferente(Atributo atributo) {
		if (this.valor == null)
			return false;
		else
			return this.valor.diferente(atributo.obterValor());
	}

	public boolean igual(Atributo atributo) {
		if (this.valor == null)
			return false;
		else
			return this.valor.igual(atributo.obterValor());
	}

	public boolean maiorIgual(Atributo atributo) {
		if (this.valor == null)
			return false;
		else
			return this.valor.maiorIgual(atributo.obterValor());
	}

	public boolean maiorQue(Atributo atributo) {
		if (this.valor == null)
			return false;
		else
			return this.valor.maior(atributo.obterValor());
	}

	public boolean menorIgual(Atributo atributo) {
		if (this.valor == null)
			return false;
		else
			return this.valor.menorIgual(atributo.obterValor());
	}

	public boolean menorQue(Atributo atributo) {
		if (this.valor == null)
			return false;
		else
			return this.valor.menor(atributo.obterValor());
	}

	public void multiplicarValor(Atributo atributo) {
		if (this.valor != null)
			this.valor.multiplicar(atributo.obterValor());
	}

	public String obterDescricao() {
		StringBuffer s = new StringBuffer();
		s.append(this.nome);
		s.append("=");
		if (this.valor == null)
			s.append("nulo");
		else if (this.valor.obterTipo() == Valor.NUMERO)
			s.append(this.valor.obterValorNumerico());
		else
			s.append("'" + this.valor.obterValorCaracter() + "'");
		return s.toString();
	}

	public String obterNome() {
		return this.nome;
	}

	public Valor obterValor() {
		return this.valor;
	}

	public void subtrairValor(Atributo atributo) {
		if (this.valor != null)
			this.valor.subtrair(atributo.obterValor());
	}
}