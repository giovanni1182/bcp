package com.gvs.crm.avaliador;

import infra.model.persistence.PersistentEntity;

public class Regra extends PersistentEntity {
	private Conjunto conjunto;

	public long obterId() throws Exception {
		return this.getId();
	}

	public String obterOperando1() throws Exception {
		return this.getProperty("operando1");
	}

	public String obterOperador() throws Exception {
		return this.getProperty("operador");
	}

	public String obterOperando2() throws Exception {
		return this.getProperty("operando2");
	}

	public String obterMensagem() throws Exception {
		return this.getProperty("mensagem");
	}

	public Regra obterProximaRegra() throws Exception {
		RegraHome home = (RegraHome) this.getModelManager()
				.getHome("RegraHome");
		return home.obterProximaRegra(this);
	}

	public Regra obterSuperRegra() throws Exception {
		RegraHome home = (RegraHome) this.getModelManager()
				.getHome("RegraHome");
		return home.obterSuperRegra(this);
	}

	public Regra obterSubRegraSe() throws Exception {
		RegraHome home = (RegraHome) this.getModelManager()
				.getHome("RegraHome");
		return home.obterSubRegraSe(this);
	}

	public Regra obterSubRegraSenao() throws Exception {
		RegraHome home = (RegraHome) this.getModelManager()
				.getHome("RegraHome");
		return home.obterSubRegraSenao(this);
	}

	public String obterDescricao() throws Exception {
		StringBuffer s = new StringBuffer();
		s.append(this.obterOperando1());
		s.append(" ");
		s.append(this.obterOperador());
		s.append(" ");
		s.append(this.obterOperando2());
		String mensagem = this.obterMensagem();
		if (!mensagem.equals("")) {
			s.append(" -> ");
			s.append(mensagem);
		}
		return s.toString();
	}

	public void atribuirOperando1(String operando1) throws Exception {
		this.setProperty("operando1", operando1);
	}

	public void atribuirOperador(String operador) throws Exception {
		this.setProperty("operador", operador);
	}

	public void atribuirOperando2(String operando2) throws Exception {
		this.setProperty("operando2", operando2);
	}

	public void atribuirSuperRegra(Regra superRegra) throws Exception {
		this.setLink("superRegra", (Regra) superRegra);
	}

	public void atribuirSubRegraSe(Regra subRegra) throws Exception {
		this.setLink("subRegraSe", (Regra) subRegra);
	}

	public void atribuirSubRegraSenao(Regra subRegra) throws Exception {
		this.setLink("subRegraSenao", (Regra) subRegra);
	}

	public void atribuirProximaRegra(Regra proximaRegra) throws Exception {
		this.setLink("proximaRegra", (Regra) proximaRegra);
	}

	public void atribuirMensagem(String mensagem) throws Exception {
		this.setProperty("mensagem", mensagem);
	}

	public void incluir(Conjunto conjunto) throws Exception {
		this.insert();
		this.conjunto = conjunto;
		this.setLink("conjunto", (Conjunto) conjunto);
		((Conjunto) conjunto).addLink("regras", (Regra) this);
	}

	public void atualizar() throws Exception {
		this.update();
	}

	public void excluir() throws Exception {
		this.delete();
	}

	public Decisao avaliar(Atributos atributos) throws Exception {
		Decisao avaliacaoRegra = new Decisao();

		String o1 = this.obterOperando1();
		Atributo operando1 = atributos.obterAtributo(o1);
		if (operando1 == null) {
			operando1 = new Atributo(o1);
			atributos.adicionarAtributo(operando1);
		}
		String o2 = this.obterOperando2();
		Atributo operando2 = atributos.obterAtributo(o2);
		if (operando2 == null) {
			operando2 = new Atributo(o2);
			atributos.adicionarAtributo(operando2);
		}
		int decisao = Decisao.DECISAO_NULA;
		String operador = this.obterOperador();
		if (operador.equals("="))
			decisao = operando1.igual(operando2) ? Decisao.DECISAO_APROVADA
					: Decisao.DECISAO_REJEITADA;
		else if (operador.equals("<>"))
			decisao = operando1.diferente(operando2) ? Decisao.DECISAO_APROVADA
					: Decisao.DECISAO_REJEITADA;
		else if (operador.equals("<"))
			decisao = operando1.menorQue(operando2) ? Decisao.DECISAO_APROVADA
					: Decisao.DECISAO_REJEITADA;
		else if (operador.equals("<="))
			decisao = operando1.menorIgual(operando2) ? Decisao.DECISAO_APROVADA
					: Decisao.DECISAO_REJEITADA;
		else if (operador.equals(">"))
			decisao = operando1.maiorQue(operando2) ? Decisao.DECISAO_APROVADA
					: Decisao.DECISAO_REJEITADA;
		else if (operador.equals(">="))
			decisao = operando1.maiorIgual(operando2) ? Decisao.DECISAO_APROVADA
					: Decisao.DECISAO_REJEITADA;
		else if (operador.equals(":=")) {
			operando1.atribuirValor(operando2);
			decisao = Decisao.DECISAO_APROVADA;
		} else if (operador.equals("+=")) {
			operando1.acumularValor(operando2);
			decisao = Decisao.DECISAO_APROVADA;
		} else if (operador.equals("-=")) {
			operando1.subtrairValor(operando2);
			decisao = Decisao.DECISAO_APROVADA;
		} else if (operador.equals("*=")) {
			operando1.multiplicarValor(operando2);
			decisao = Decisao.DECISAO_APROVADA;
		} else
			throw new Exception("Operador '" + this.obterOperador()
					+ "' não implementado");
		avaliacaoRegra.atribuirDecisao(decisao);

		if (decisao == Decisao.DECISAO_REJEITADA) {
			String mensagem = this.obterMensagem();
			if (!mensagem.equals("")) {
				avaliacaoRegra.adicionarMensagem(mensagem);
			}
		}

		return avaliacaoRegra;
	}
}