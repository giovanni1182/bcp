package com.gvs.crm.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Impressao;

import infra.config.InfraProperties;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class ImpressaoImpl extends EventoImpl implements Impressao {
	/*
	 * create table impressao_componente( id int(11) not null, nome varchar(32),
	 * ordem bigint(20), primary key(id,ordem) ) type = innodb;
	 * 
	 * create table impressao_componente_descricao( id int(11) not null, nome
	 * varchar(32), descricao text, primary key(id,nome) ) type = innodb;
	 * 
	 * ALTER TABLE impressao_componente DROP PRIMARY KEY, ADD PRIMARY KEY
	 * (id,ordem,nome); ALTER TABLE impressao_componente_descricao ADD ordem
	 * bigint(20); ALTER TABLE impressao_componente_descricao DROP PRIMARY KEY,
	 * ADD PRIMARY KEY (id,ordem,nome); ALTER TABLE impressao_componente ADD
	 * link_variavel int(11); ALTER TABLE impressao_componente ADD negrito
	 * int(1); ALTER TABLE impressao_componente ADD italico int(1); ALTER TABLE
	 * impressao_componente ADD sublinhado int(1); ALTER TABLE
	 * impressao_componente ADD salto int(5);
	 * 
	 * ALTER TABLE `impressao_componente` DROP PRIMARY KEY, ADD PRIMARY KEY
	 * (id,ordem,nome,link_variavel)
	 *  
	 */
	private class ComponenteImpl implements Componente {

		private String nome;

		private long ordem;

		private String descricao;

		private Evento linkVariavel;

		private boolean negrito = false;

		private boolean italico = false;

		private boolean sublinhado = false;

		private int salto = 0;

		private boolean visivel = true;

		public ComponenteImpl(String nome, long ordem, String descricao,
				Evento linkVariavel, boolean negrito, boolean italico,
				boolean sublinhado, int salto, boolean visivel) {
			this.nome = nome;
			this.ordem = ordem;
			this.descricao = descricao;
			this.linkVariavel = linkVariavel;
			this.negrito = negrito;
			this.italico = italico;
			this.sublinhado = sublinhado;
			this.salto = salto;
			this.visivel = visivel;

		}

		public boolean estaVisivel() {
			return this.visivel;
		}

		public String obterDescricao() {
			return this.descricao;
		}

		public String obterComponenteNome() {
			return this.nome;
		}

		public long obterComponenteOrdem() {
			return this.ordem;
		}

		public Evento obterLinkVariavel() {
			return this.linkVariavel;
		}

		public int obterSaltoDeLinha() {
			return this.salto;
		}

		public boolean permiteNegrito() {
			return this.negrito;
		}

		public boolean permiteItalico() {
			return this.italico;
		}

		public boolean permiteSublinhado() {
			return this.sublinhado;
		}
	}

	private int saltoDeLinha = 0;

	public void setNextSalto(int salto) throws Exception {
		this.saltoDeLinha = salto;
	}

	public void incluirComponente(String componente, String descricao,
			boolean negrito, boolean italico, boolean sublinhado)
			throws Exception {

		long ordem = this.verificarOrdem(new Date().getTime());

		int neg = 0;
		int ita = 0;
		int sub = 0;
		if (negrito)
			neg = 1;
		if (italico)
			ita = 1;
		if (sublinhado)
			sub = 1;

		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate(
						"insert into impressao_componente (id,nome,ordem,negrito,italico,sublinhado,salto) values (?, ?, ?, ?, ?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addString(componente);
		insert.addLong(ordem);
		insert.addInt(neg);
		insert.addInt(ita);
		insert.addInt(sub);
		insert.addInt(this.saltoDeLinha);
		insert.execute();

		if (descricao != null)
			this.incluirComponenteDescricao(componente, descricao, ordem);

		this.saltoDeLinha = 0;
	}

	public void incluirComponente(String componente, String descricao,
			long linkVariavel, boolean negrito, boolean italico,
			boolean sublinhado) throws Exception {

		long ordem = this.verificarOrdem(new Date().getTime());

		int neg = 0;
		int ita = 0;
		int sub = 0;
		if (negrito)
			neg = 1;
		if (italico)
			ita = 1;
		if (sublinhado)
			sub = 1;

		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate(
						"insert into impressao_componente (id,nome,ordem,link_variavel,negrito,italico,sublinhado,salto) values (?, ?, ?, ?, ?, ?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addString(componente);
		insert.addLong(ordem);
		insert.addLong(linkVariavel);
		insert.addInt(neg);
		insert.addInt(ita);
		insert.addInt(sub);
		insert.addInt(this.saltoDeLinha);
		insert.execute();
		if (descricao != null)
			this.incluirComponenteDescricao(componente, descricao, ordem);

		this.saltoDeLinha = 0;
	}

	public void incluirComponenteComOrdem(String componente, String descricao,
			long ordem, boolean negrito, boolean italico, boolean sublinhado)
			throws Exception {

		int neg = 0;
		int ita = 0;
		int sub = 0;
		if (negrito)
			neg = 1;
		if (italico)
			ita = 1;
		if (sublinhado)
			sub = 1;

		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate(
						"insert into impressao_componente (id,nome,ordem,negrito,italico,sublinhado,salto) values (?, ?, ?, ?, ?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addString(componente);
		insert.addLong(ordem);
		insert.addInt(neg);
		insert.addInt(ita);
		insert.addInt(sub);
		insert.addInt(this.saltoDeLinha);
		insert.execute();

		if (descricao != null)
			this.incluirComponenteDescricao(componente, descricao, ordem);

		this.saltoDeLinha = 0;
	}

	public void atualizarComponente(String componente, long ordem,
			long ordemAtual) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate(
						"update impressao_componente set ordem=? where id=? and nome=? and ordem=?");
		update.addLong(ordem);
		update.addLong(this.obterId());
		update.addString(componente);
		update.addLong(ordemAtual);
		update.execute();

		if (componente.equals("componentedescritivo")) {
			SQLUpdate update2 = this
					.getModelManager()
					.createSQLUpdate(
							"update impressao_componente_descricao set ordem=? where id=? and nome=? and ordem=?");
			update2.addLong(ordem);
			update2.addLong(this.obterId());
			update2.addString(componente);
			update2.addLong(ordemAtual);
			update2.execute();
		}
	}

	public void atualizarComponenteVisivel(int visivel, long ordem,
			Impressao impressao) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate(
						"update impressao_componente set visivel=? where id=? and ordem=?");
		update.addLong(visivel);
		update.addLong(impressao.obterId());
		update.addLong(ordem);
		update.execute();

		System.out.println("update impressao_componente set visivel=" + visivel
				+ " where id=" + impressao.obterId() + " and ordem=" + ordem);
	}

	public void incluirComponenteDescricao(String componente, String descricao,
			long ordem) throws Exception {
		SQLUpdate insert2 = this
				.getModelManager()
				.createSQLUpdate(
						"insert into impressao_componente_descricao (id,nome,descricao,ordem) values (?, ?, ?, ?)");
		insert2.addLong(this.obterId());
		insert2.addString(componente);
		insert2.addString(descricao);
		insert2.addLong(ordem);
		insert2.execute();
	}

	public Collection obterComponentes() throws Exception {

		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome(
				"EventoHome");
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select nome, ordem, link_variavel, negrito, italico, sublinhado, salto, visivel from impressao_componente where id=?");
		query.addLong(this.obterId());
		SQLRow[] rows = query.execute();
		Map componentes = new HashMap();
		for (int i = 0; i < rows.length; i++) {
			String nome = rows[i].getString("nome");
			long ordem = rows[i].getLong("ordem");
			long variavel = rows[i].getLong("link_variavel");

			boolean negritoBoolean = false;
			boolean italicoBoolean = false;
			boolean sublinhadoBoolean = false;
			boolean visivelBoolean = true;

			int negrito = rows[i].getInt("negrito");
			int italico = rows[i].getInt("italico");
			int sublinhado = rows[i].getInt("sublinhado");
			int salto = rows[i].getInt("salto");
			int visivel = rows[i].getInt("visivel");

			if (negrito != 0)
				negritoBoolean = true;
			if (italico != 0)
				italicoBoolean = true;
			if (sublinhado != 0)
				sublinhadoBoolean = true;
			if (visivel == 1)
				visivelBoolean = false;

			Evento linkVariavel = null;
			if (variavel != 0)
				linkVariavel = eventoHome.obterEventoPorId(variavel);

			SQLQuery query2 = this
					.getModelManager()
					.createSQLQuery(
							"crm",
							"select descricao from impressao_componente_descricao where id=? and nome=? and ordem=?");
			query2.addLong(this.obterId());
			query2.addString(nome);
			query2.addLong(ordem);
			String descricao = query2.executeAndGetFirstRow().getString(
					"descricao");

			Componente componente = new ComponenteImpl(nome, ordem, descricao,
					linkVariavel, negritoBoolean, italicoBoolean,
					sublinhadoBoolean, salto, visivelBoolean);
			componentes.put(ordem + variavel + nome, componente);
		}
		return componentes.values();
	}

	public void excluirComponente(String componente, long ordem)
			throws Exception {

		SQLUpdate delete1 = this.getModelManager().createSQLUpdate(
				"delete from impressao_componente where id=? and ordem=?");
		delete1.addLong(this.obterId());
		delete1.addLong(ordem);
		delete1.execute();
		if (componente.equals("componentedescritivo")
				|| componente.equals("componentedescritivoLink")) {
			SQLUpdate delete2 = this
					.getModelManager()
					.createSQLUpdate(
							"delete from impressao_componente_descricao where id=? and nome=? and ordem=?");
			delete2.addLong(this.obterId());
			delete2.addString(componente);
			delete2.addLong(ordem);
			delete2.execute();
		}
	}

	public Collection obterListaComponentes() throws Exception {
		String s = InfraProperties.getInstance().getProperty(
				"impressao.componentes");
		StringTokenizer st = new StringTokenizer(s, ",");
		Collection componentes = new ArrayList();
		while (st.hasMoreTokens())
			componentes.add(st.nextToken());
		return componentes;
	}

	public void ordenarComponente(long ordem, String direcao) throws Exception {

		HashMap ordenados = new HashMap();
		long ordemUm = 0;
		long ordemDois = 0;
		String nomeUm = "";
		String nomeDois = "";

		for (Iterator i = obterComponentes().iterator(); i.hasNext();) {
			Impressao.Componente c = (Impressao.Componente) i.next();
			ordenados.put(new Long(c.obterComponenteOrdem()), c);
		}

		if (direcao.equals("cima")) {

			Impressao.Componente componenteUm = (Impressao.Componente) ordenados
					.get(new Long(ordem));
			if (componenteUm != null) {
				ordemUm = ordem;
				nomeUm = componenteUm.obterComponenteNome();
			}
			Impressao.Componente componenteDois = obterComponenteACima(ordem);
			if (componenteDois != null) {
				ordemDois = componenteDois.obterComponenteOrdem();
				nomeDois = componenteDois.obterComponenteNome();
			}
			if (ordemUm != 0 && ordemDois != 0) {
				this.atualizarComponente(nomeDois, 1, ordemDois);
				this.atualizarComponente(nomeUm, 2, ordemUm);

				this.atualizarComponente(nomeDois, ordemUm, 1);
				this.atualizarComponente(nomeUm, ordemDois, 2);
			} else {
				throw new Exception("Não pode ser ordenado para cima");
			}

		} else {

			Impressao.Componente componenteUm = (Impressao.Componente) ordenados
					.get(new Long(ordem));
			if (componenteUm != null) {
				ordemUm = ordem;
				nomeUm = componenteUm.obterComponenteNome();
			}
			Impressao.Componente componenteDois = obterComponenteABaixo(ordem);
			if (componenteDois != null) {
				ordemDois = componenteDois.obterComponenteOrdem();
				nomeDois = componenteDois.obterComponenteNome();
			}
			if (ordemUm != 0 && ordemDois != 0) {
				this.atualizarComponente(nomeDois, 1, ordemDois);
				this.atualizarComponente(nomeUm, 2, ordemUm);

				this.atualizarComponente(nomeDois, ordemUm, 1);
				this.atualizarComponente(nomeUm, ordemDois, 2);
			} else
				throw new Exception("Não pode ser ordenado para baixo");
		}
	}

	private Impressao.Componente obterComponenteACima(long ordem)
			throws Exception {
		long retorno = 0;
		Impressao.Componente retorna = null;
		for (Iterator i = obterComponentes().iterator(); i.hasNext();) {
			Impressao.Componente c = (Impressao.Componente) i.next();
			if (c.obterComponenteOrdem() < ordem) {
				if (retorno < c.obterComponenteOrdem()) {
					retorno = c.obterComponenteOrdem();
					retorna = c;
				}
			}
		}
		return (Impressao.Componente) retorna;
	}

	private Impressao.Componente obterComponenteABaixo(long ordem)
			throws Exception {
		long retorno = 0;
		Impressao.Componente retorna = null;
		for (Iterator i = obterComponentes().iterator(); i.hasNext();) {
			Impressao.Componente c = (Impressao.Componente) i.next();
			if (c.obterComponenteOrdem() > ordem) {
				if (retorno > c.obterComponenteOrdem() || retorno == 0) {
					retorno = c.obterComponenteOrdem();
					retorna = c;
				}
			}
		}
		return (Impressao.Componente) retorna;
	}

	public void excluir() throws Exception {
		super.excluir();

		/*
		 * SQLUpdate update = this.getModelManager().createSQLUpdate("delete
		 * from impressao_componente where id=?");
		 * update.addLong(this.obterId()); update.execute(); SQLUpdate update2 =
		 * this.getModelManager().createSQLUpdate("delete from
		 * impressao_componente_descricao where id=?");
		 * update2.addLong(this.obterId()); update2.execute();
		 */

	}

	/*
	 * Não permite datas iguais! utilização deste reduz a performace.
	 */
	private long verificarOrdem(long valor) throws Exception {
		long valorAux = new Date().getTime();

		//System.out.println("Entra no Loop");

		//int saiDoLoop = 0;

		while (valor <= valorAux) {
			SQLQuery query = this.getModelManager().createSQLQuery(
					"select max(ordem) from impressao_componente");
			valorAux = query.executeAndGetFirstRow().getLong("max(ordem)") + 1;

			if (valorAux > valor)
				break;
		}

		if (valorAux == 1 || valorAux == 0)
			return new Date().getTime();
		else
			return valorAux;
	}

	public void ordenarComponenteDistante(long aSer, long aPos)
			throws Exception {
		Map componentes = new TreeMap();
		long comeco;

		for (Iterator i = obterComponentes().iterator(); i.hasNext();) {
			Impressao.Componente c = (Impressao.Componente) i.next();
			if (c.obterComponenteOrdem() >= aSer
					&& c.obterComponenteOrdem() < aPos) {
				componentes.put(c.obterComponenteOrdem() + " "
						+ c.obterComponenteNome(), c);
			} else if (c.obterComponenteOrdem() >= aPos
					&& c.obterComponenteOrdem() <= aSer) {
				componentes.put(c.obterComponenteOrdem() + " "
						+ c.obterComponenteNome(), c);
			}
		}
		int j = 0;

		Impressao.Componente[] aCima = new Impressao.Componente[componentes
				.size() + 1];

		for (Iterator i = componentes.values().iterator(); i.hasNext();) {
			Impressao.Componente c = (Impressao.Componente) i.next();
			if (aSer < aPos)
				this.ordenarComponente(c.obterComponenteOrdem(), "baixo");
			else {
				aCima[j] = c;
				j += 1;
			}
		}

		if (aSer > aPos) {
			for (int i = componentes.size() - 1; i >= 2; i--) {
				Impressao.Componente c = (Impressao.Componente) aCima[i];
				this.ordenarComponente(c.obterComponenteOrdem(), "cima");
			}
		}
	}
}