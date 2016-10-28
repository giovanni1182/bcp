package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.gvs.crm.component.NivelSelect;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Conta;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.BasicView;
import infra.view.Block;
import infra.view.Button;
import infra.view.Image;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class RelatorioOutraMoedaView extends BasicView {
	private Entidade entidade, origemMenu;

	private Collection entidades2 = new ArrayList();

	private boolean usuarioAtual;

	private String nivelString;

	private String mes;

	private String ano;

	private String mesAno;

	public RelatorioOutraMoedaView(Entidade entidade, Collection entidades,
			Entidade origemMenu, boolean usuarioAtual, String nivel,
			String mes, String ano) throws Exception {
		this.entidade = entidade;
		this.entidades2 = entidades;
		this.origemMenu = origemMenu;
		this.usuarioAtual = usuarioAtual;
		this.nivelString = nivel;
		if (mes.equals("")) {
			this.mes = new SimpleDateFormat("MM").format(new Date());
			this.ano = new SimpleDateFormat("yyyy").format(new Date());

		} else {
			this.mes = mes;
			this.ano = ano;
		}

		this.mesAno = this.mes + this.ano;
	}

	private Collection entidades = new ArrayList();

	private Collection inferiores = new ArrayList();

	private Collection entidadesSuperiores = new ArrayList();

	private Map niveis = new HashMap();

	private double totalMoedaEstrangeira = 0;

	private double totalGuarani = 0;

	private void adicionarEntidade(Entidade seguradora, Entidade entidade,
			int nivel, boolean selecionado, Table table) throws Exception {
		if (entidade instanceof ClassificacaoContas
				|| entidade instanceof Conta) {
			if (!this.entidades.contains(entidade)) {
				boolean diferenteZero = false;

				if (entidade instanceof ClassificacaoContas) {
					ClassificacaoContas cContas = (ClassificacaoContas) entidade;

					if ((cContas.obterTotalizacaoExistenteOutraMoeda(
							seguradora, this.mesAno) != 0 || cContas
							.obterTotalizacaoExistente(seguradora, this.mesAno) != 0))
						diferenteZero = true;
				}

				else if (entidade instanceof Conta) {
					Conta conta = (Conta) entidade;

					if ((conta.obterTotalizacaoExistenteOutraMoeda(seguradora,
							this.mesAno) != 0 || conta
							.obterTotalizacaoExistente(seguradora, this.mesAno) != 0))
						diferenteZero = true;
				}

				if (diferenteZero) {
					Table t = new Table(6);
					t.addStyle(Table.STYLE_BLANK);
					if (nivel != 0)
						t.add(new Space(nivel + 4));

					if (nivel != 0)
						niveis
								.put(entidade.obterNome(), new Integer(
										nivel + 4));
					else
						niveis.put(entidade.obterNome(), new Integer(0));

					t.add(new Image(entidade.obterIcone()));
					t.add(new Space());

					if (entidade instanceof Conta) {
						Conta conta = (Conta) entidade;

						Label codigoContaLabel = new Label("");
						codigoContaLabel.setValue(conta.obterCodigo());
						codigoContaLabel.setBold(selecionado);
						table.addData(codigoContaLabel);
					} else if (entidade instanceof ClassificacaoContas) {
						ClassificacaoContas cContas = (ClassificacaoContas) entidade;

						Label codigoContaLabel = new Label("");
						codigoContaLabel.setValue(cContas.obterCodigo());
						codigoContaLabel.setBold(selecionado);
						table.addData(codigoContaLabel);
					} else
						table.addData("");

					/*
					 * if(entidade instanceof ClassificacaoContas) { Label label =
					 * new Label(entidade.obterNome()); t.add(label); } else
					 * if(entidade instanceof Conta) { Conta conta = (Conta)
					 * entidade;
					 * 
					 * Link nomeLink = new Link(conta.obterNome(), new
					 * Action("visualizarEvento"));
					 * nomeLink.getAction().add("id",
					 * conta.obterMovimentacao(seguradora, true,
					 * mesAno).obterId()); t.add(nomeLink); }
					 */

					t.add(entidade.obterNome());

					table.addData(t);

					if (entidade instanceof ClassificacaoContas) {
						this.totalMoedaEstrangeira = ((ClassificacaoContas) entidade)
								.obterTotalizacaoExistenteOutraMoeda(
										seguradora, this.mesAno);
						this.totalGuarani = ((ClassificacaoContas) entidade)
								.obterTotalizacaoExistente(seguradora,
										this.mesAno);

					}

					else if (entidade instanceof Conta) {
						Conta conta = (Conta) entidade;

						this.totalMoedaEstrangeira = conta
								.obterTotalizacaoExistente(seguradora,
										this.mesAno);
						this.totalGuarani = conta.obterTotalizacaoExistente(
								seguradora, this.mesAno);

					}

					Label valorMoedaEstrangeiraLabel = new Label(
							this.totalMoedaEstrangeira, "#,##0.00");
					valorMoedaEstrangeiraLabel.setBold(selecionado);
					table.add(valorMoedaEstrangeiraLabel);

					Label valorLabel = new Label(this.totalGuarani, "#,##0.00");
					valorLabel.setBold(selecionado);
					table.add(valorLabel);

					this.entidades.add(entidade);
					this.entidadesSuperiores.add(entidade.obterSuperior());

					if (this.nivelString.equals("Nivel 5")
							&& entidade instanceof ClassificacaoContas) {
						ClassificacaoContas cContas = (ClassificacaoContas) entidade;
						if (cContas.obterNivel().equals("Nivel 4"))
							inferiores = ((ClassificacaoContas) entidade)
									.obterInferioresNivel5(seguradora,
											this.mesAno);
					}

					for (Iterator i = inferiores.iterator(); i.hasNext();) {
						Entidade e = (Entidade) i.next();

						/*
						 * if(e instanceof ClassificacaoContas || e instanceof
						 * Conta) { if(e instanceof ClassificacaoContas) {
						 * if(nivel==0) nivel++; else { ClassificacaoContas
						 * cContas = (ClassificacaoContas) e; nivel = new
						 * Integer(niveis.get(cContas.obterSuperior().obterNome()).toString()).intValue();
						 * if(nivel == 0) nivel++; } } else {
						 */
						Conta conta = (Conta) e;
						if (niveis.containsKey(conta.obterSuperior()
								.obterNome()))
							nivel = Integer.parseInt(niveis.get(
									conta.obterSuperior().obterNome())
									.toString());
						else
							nivel = 1;
						//}

						this.adicionarEntidade(seguradora, e, nivel, false,
								table);
						//}
					}
				}
			}
		}
	}

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		Table table = new Table(4);
		table.setWidth("100%");
		table.addStyle(Table.STYLE_ALTERNATE);

		Table mesAnoTable = new Table(3);
		Block block2 = new Block(Block.HORIZONTAL);
		block2.add(new Label("Mes:"));
		block2.add(new Space());
		block2.add(new InputString("mes", this.mes, 2));
		block2.add(new Space(2));
		block2.add(new Label("Año:"));
		block2.add(new Space());
		block2.add(new InputString("ano", this.ano, 4));
		mesAnoTable.add(block2);
		//mesAnoTable.add(new MesAnoSelect2("mesAno", this.entidade,
		// this.mesAno));
		mesAnoTable.add(new Space(5));

		Action action = new Action("apagarTotalizacao");
		action.add("id", this.entidade.obterId());

		//mesAnoTable.add(new Button("Regenerar Listado", action));
		table.setNextColSpan(table.getColumns());
		table.add(mesAnoTable);

		table.setNextColSpan(table.getColumns());
		table.add(new Space());

		table.addHeader("Cuenta");

		Block block = new Block(Block.HORIZONTAL);
		block.add(new NivelSelect("nivel", this.nivelString));
		block.add(new Space(4));

		Action action2 = new Action("visualizarRelatorioAseguradora");
		action2.add("id", this.entidade.obterId());
		action2.add("lista", true);

		block.add(new Button("Visualizar", action2));

		table.add(block);

		long mesEscolhido = Long.parseLong(this.mesAno.substring(0, 2));
		long anoEscolhido = Long.parseLong(this.mesAno.substring(5, this.mesAno
				.length()));

		mesEscolhido -= 1;

		String mes = "";

		if (mesEscolhido == 0)
			mes = "12";
		else if (new Long(mesEscolhido).toString().length() == 1)
			mes = "0" + new Long(mesEscolhido).toString();
		else
			mes = new Long(mesEscolhido).toString();

		if (mes.equals("0"))
			anoEscolhido -= 1;

		//table.addHeader("Saldo " + mes + " - " + anoEscolhido);
		table.addHeader("Saldo en Moneda Extranjera");

		//table.addHeader("Saldo " + this.mesAno);
		table.addHeader("Saldo Actual");

		if (this.entidades2.size() == 0) {
			table.addData("");
			table.setNextColSpan(table.getColumns());
			table.setNextHAlign(Table.HALIGN_CENTER);

			table.addData("Ninguna Cuenta a ser visualizada.");

		} else {
			Map superiores = new TreeMap();

			for (Iterator i = this.entidades2.iterator(); i.hasNext();) {
				Entidade e = (Entidade) i.next();

				for (Iterator j = e.obterSuperiores().iterator(); j.hasNext();) {
					Entidade e2 = (Entidade) j.next();

					if (e2 instanceof ClassificacaoContas) {
						if (!e2.obterNome().equals("PLAN DE CUENTAS")) {
							ClassificacaoContas cContas = (ClassificacaoContas) e2;
							superiores.put(cContas.obterCodigo(), cContas);
						}
					}
				}
			}

			//superiores.addAll(this.entidades2);

			//Map m = new TreeMap();

			/*
			 * for(Iterator i = superiores.iterator() ; i.hasNext() ; ) {
			 * Entidade e = (Entidade) i.next(); if(e instanceof
			 * ClassificacaoContas) { ClassificacaoContas cContas =
			 * (ClassificacaoContas) e; m.put(cContas.obterCodigo(), cContas); } }
			 */

			for (Iterator i = superiores.values().iterator(); i.hasNext();) {
				int nivel = 0;

				ClassificacaoContas cContas = (ClassificacaoContas) i.next();

				if (this.entidades.contains(cContas.obterSuperior())) {
					nivel = new Integer(niveis.get(
							cContas.obterSuperior().obterNome()).toString())
							.intValue();
					if (nivel == 0)
						nivel = 1;
				}

				this.adicionarEntidade(this.entidade, cContas, nivel, false,
						table);
			}
		}
		return table;
	}

	public String getSelectedGroup() throws Exception {
		if (usuarioAtual)
			return "Menu Prncipal";
		else
			return this.entidade.obterNome();
	}

	public String getSelectedOption() throws Exception {
		return "Hierarquia";
	}

	public View getTitle() throws Exception {
		return new Label("Hierarquia");
	}

	public Entidade getOrigemMenu() throws Exception {
		if (this.origemMenu.obterId() != 0)
			return this.origemMenu;
		else
			return this.entidade;
	}
}