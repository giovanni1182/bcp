package com.gvs.crm.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.Border;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Conta;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Produto;

import infra.control.Action;
import infra.security.User;
import infra.view.BasicView;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class PopupEntidadesView extends BasicView {
	private String campoEntidadeId;

	private String campoEntidadeNome;

	private String tipoFixo;

	private String tipo;

	private String nome;

	private boolean empUsuPes;

	public PopupEntidadesView(String campoEntidadeId, String campoEntidadeNome,
			String tipoFixo, String tipo, String nome, boolean empUsuPes) {
		this.campoEntidadeId = campoEntidadeId;
		this.campoEntidadeNome = campoEntidadeNome;
		this.tipoFixo = tipoFixo;
		this.tipo = tipo;
		this.nome = nome;
		this.empUsuPes = empUsuPes;
		if (this.tipoFixo.length() > 0)
			this.tipo = this.tipoFixo;
	}

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(user);
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Table table = new Table(2);
		table.setWidth("100%");
		table.addHeader("Nombre:");
		table.addData(new InputString("nome", this.nome, 40));
		Action popupEntidadesAction = new Action("popupEntidades");
		popupEntidadesAction.add("campoEntidadeId", this.campoEntidadeId);
		popupEntidadesAction.add("campoEntidadeNome", this.campoEntidadeNome);
		popupEntidadesAction.add("tipoFixo", this.tipoFixo);
		popupEntidadesAction.add("empUsuPes", this.empUsuPes);
		table.setNextColSpan(table.getColumns());
		table.setNextHAlign(Table.HALIGN_CENTER);
		Block block = new Block(Block.HORIZONTAL);
		block.add(new Button("Buscar", popupEntidadesAction));
		block.add(new Space(2));
		block.add(new Button("Cancelar", new Action(Action.CLOSE)));
		table.addData(block);
		Collection entidades = new ArrayList();
		if (this.nome.length() > 0)
			if (this.empUsuPes)
				entidades = entidadeHome.localizarEntidadesEmpUsuPes(this.nome);
			else
				entidades = entidadeHome.localizarEntidades(this.nome,
						this.tipo);
		if (entidades.size() > 0) {
			Table entidadesTable = new Table(3);
			entidadesTable.setWidth("100%");
			entidadesTable.addStyle(Table.STYLE_ALTERNATE);
			entidadesTable.addHeader("Id");
			entidadesTable.addHeader("Nombre");
			entidadesTable.addHeader("Tipo");
			for (Iterator i = entidades.iterator(); i.hasNext();) {
				Entidade entidade = (Entidade) i.next();
				entidadesTable.addData(new Label(entidade.obterId()));
				Action returnAction = new Action(Action.RETURN);
				returnAction.add(this.campoEntidadeId, entidade.obterId());

				if (entidade instanceof Produto) {
					Produto produto = (Produto) entidade;
					returnAction.add(this.campoEntidadeNome, produto
							.obterNome()
							+ " " + produto.obterDimensao());
					entidadesTable.addData(new Link(produto.obterNome() + " "
							+ produto.obterDimensao(), returnAction));

				} else if (entidade instanceof Conta) {
					Conta conta = (Conta) entidade;
					returnAction.add(this.campoEntidadeNome, conta
							.obterCodigo()
							+ " " + conta.obterNome());
					entidadesTable.addData(new Link(conta.obterCodigo() + " "
							+ conta.obterNome(), returnAction));
				} else {
					returnAction.add(this.campoEntidadeNome, entidade
							.obterNome());
					entidadesTable.addData(new Link(entidade.obterNome(),
							returnAction));
				}

				entidadesTable.addData(new Label(entidade
						.obterDescricaoClasse()));

				if (entidade instanceof Produto) {
					Produto produto = (Produto) entidade;
					if (!produto.obterInferiores().isEmpty())
						this.adicionarInferiores(produto.obterInferiores(), 0,
								entidadesTable);
				}

			}
			table.setNextColSpan(table.getColumns());
			table.addData(entidadesTable);
		} else {
			table.setNextColSpan(table.getColumns());
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Ningúna entidad a ser visualizada.");
		}
		return new Border(table);
	}

	private void adicionarInferiores(Collection inferiores, int nivel,
			Table table) throws Exception {
		for (Iterator j = inferiores.iterator(); j.hasNext();) {
			Entidade componente = (Entidade) j.next();
			Block blockComponente = new Block(Block.HORIZONTAL);
			table.add("");
			blockComponente.add(new Space(nivel * 4));
			blockComponente.add(new Space(3));
			//Produto produto = (Produto) componente;
			blockComponente.add(new Label(componente.obterNome()));
			table.add(blockComponente);
			table.add(componente.obterClasse());
		}
	}
}