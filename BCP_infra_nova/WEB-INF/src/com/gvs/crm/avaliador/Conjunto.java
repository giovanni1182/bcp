package com.gvs.crm.avaliador;

import java.text.SimpleDateFormat;
import java.util.Date;

import infra.model.persistence.PersistentEntity;

public class Conjunto extends PersistentEntity {
	private final int SUB_REGRA_SE = 0;

	private final int SUB_REGRA_SENAO = 1;

	public void atribuirDescricao(String descricao) throws Exception {
		this.setProperty("descricao", descricao);
	}

	public void atribuirNome(String nome) throws Exception {
		this.setProperty("nome", nome);
	}

	public void atribuirPrimeiraRegra(Regra primeiraRegra) throws Exception {
		this.setLink("primeiraRegra", (Regra) primeiraRegra);
	}

	public void atualizar() throws Exception {
		this.setProperty("atualizacao",
				new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
		this.update();
	}

	public Decisao avaliar(Atributos atributos) throws Exception {
		Decisao avaliacaoConjunto = new Decisao();
		avaliacaoConjunto.atribuirDecisao(Decisao.DECISAO_APROVADA);
		Regra regra = this.obterPrimeiraRegra();
		while (regra != null) {
			Decisao avaliacaoRegra = regra.avaliar(atributos);
			Regra subRegraSe = regra.obterSubRegraSe();
			Regra subRegraSenao = regra.obterSubRegraSenao();
			Regra proximaRegra = regra.obterProximaRegra();
			if (subRegraSe == null)
				avaliacaoConjunto.agregar(avaliacaoRegra);
			if (avaliacaoRegra.obterDecisao() == Decisao.DECISAO_APROVADA
					&& subRegraSe != null) {
				regra = subRegraSe;
			} else if (avaliacaoRegra.obterDecisao() == Decisao.DECISAO_REJEITADA
					&& subRegraSenao != null) {
				regra = subRegraSenao;
			} else if (proximaRegra != null) {
				regra = proximaRegra;
			} else {
				Regra superRegra = regra.obterSuperRegra();
				while (superRegra != null) {
					proximaRegra = superRegra.obterProximaRegra();
					if (proximaRegra != null) {
						regra = proximaRegra;
						break;
					}
					superRegra = superRegra.obterSuperRegra();
				}
				if (superRegra == null)
					regra = null;
			}
		}
		return avaliacaoConjunto;
	}

	public void excluir() throws Exception {
		RegraHome home = (RegraHome) this.getModelManager()
				.getHome("RegraHome");
		Regra[] regras = home.obterRegrasPorConjunto(this);
		for (int i = 0; i < regras.length; i++)
			regras[i].excluir();
		this.delete();
	}

	public String exportarRegras() throws Exception {
		StringBuffer s = new StringBuffer();
		s.append("inicio\n");
		Regra regra = this.obterPrimeiraRegra();
		if (regra != null)
			s.append(formatarRegras(regra, 1));
		s.append("final\n");
		return s.toString();
	}

	private String formatarRegras(Regra regra, int identacao) throws Exception {
		StringBuffer espacos = new StringBuffer();
		for (int i = 0; i < identacao; i++)
			espacos.append("   ");
		StringBuffer s = new StringBuffer();
		while (regra != null) {
			Regra subRegraSe = regra.obterSubRegraSe();
			if (subRegraSe != null) {
				s.append(espacos);
				s.append("se ");
				s.append(regra.obterDescricao());
				s.append("\n");
				s.append(this.formatarRegras(subRegraSe, identacao + 1));
				Regra subRegraSenao = regra.obterSubRegraSenao();
				if (subRegraSenao != null) {
					s.append(espacos);
					s.append("senao\n");
					s.append(this.formatarRegras(subRegraSenao, identacao + 1));
				}
				s.append(espacos);
				s.append("fimse\n");
			} else {
				s.append(espacos);
				s.append(regra.obterDescricao());
				s.append("\n");
			}
			regra = regra.obterProximaRegra();
		}
		return s.toString();
	}

	public void importarRegras(String regrasString) throws Exception {
		// excluir as regra atuais
		RegraHome home = (RegraHome) this.getModelManager()
				.getHome("RegraHome");
		Regra[] regras = home.obterRegrasPorConjunto(this);
		for (int i = 0; i < regras.length; i++)
			regras[i].excluir();
		// importar as novas regras
		Tokenizer tokens = new Tokenizer(regrasString);
		if (!"inicio".equals(tokens.nextToken()))
			throw new Exception("'inicio' esperado");
		tokens.nextToken();
		this.interpretarRegras(tokens, null, 0);
		if (!"final".equals(tokens.currentToken()))
			throw new Exception("'final' esperado");
	}

	public void incluir() throws Exception {
		this.setProperty("atualizacao",
				new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
		this.insert();
	}

	private void interpretarRegras(Tokenizer tokens, Regra superRegra,
			int modoSubRegra) throws Exception {
		Regra anterior = null;
		while (true) {
			if (tokens.currentToken().equals("final"))
				break;
			else if (tokens.currentToken().equals("senao"))
				break;
			else if (tokens.currentToken().equals("fimse"))
				break;
			Regra regra = (Regra) this.getModelManager().getEntity("Regra");
			regra.incluir(this);
			if (tokens.currentToken().equals("se")) {
				regra.atribuirOperando1(tokens.nextToken());
				regra.atribuirOperador(tokens.nextToken());
				regra.atribuirOperando2(tokens.nextToken());
				tokens.nextToken();
				this.interpretarRegras(tokens, regra, this.SUB_REGRA_SE);
				if ("senao".equals(tokens.currentToken())) {
					tokens.nextToken();
					this.interpretarRegras(tokens, regra, this.SUB_REGRA_SENAO);
				}
				if (!"fimse".equals(tokens.currentToken()))
					throw new Exception("'fimse' esperado");
				tokens.nextToken();
			} else {
				regra.atribuirOperando1(tokens.currentToken());
				regra.atribuirOperador(tokens.nextToken());
				regra.atribuirOperando2(tokens.nextToken());
				if ("->".equals(tokens.nextToken())) {
					regra.atribuirMensagem(tokens.nextToken());
					tokens.nextToken();
				}
			}
			if (anterior == null) {
				if (superRegra == null)
					this.atribuirPrimeiraRegra(regra);
				else if (modoSubRegra == this.SUB_REGRA_SE)
					superRegra.atribuirSubRegraSe(regra);
				else
					superRegra.atribuirSubRegraSenao(regra);
			} else {
				anterior.atribuirProximaRegra(regra);
			}
			regra.atribuirSuperRegra(superRegra);
			regra.atualizar();
			anterior = regra;
		}
	}

	public Date obterAtualizacao() throws Exception {
		String atualizacao = this.getProperty("atualizacao");
		if ("".equals(atualizacao))
			return null;
		else
			return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(atualizacao);
	}

	public String obterDescricao() throws Exception {
		return this.getProperty("descricao");
	}

	public long obterId() throws Exception {
		return this.getId();
	}

	public String obterNome() throws Exception {
		return this.getProperty("nome");
	}

	public Regra obterPrimeiraRegra() throws Exception {
		RegraHome home = (RegraHome) this.getModelManager()
				.getHome("RegraHome");
		return home.obterRegraPorConjunto(this);
	}
}