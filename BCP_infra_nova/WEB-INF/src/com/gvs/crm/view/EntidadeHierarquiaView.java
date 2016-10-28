package com.gvs.crm.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Conta;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Usuario;

import infra.control.Action;
import infra.security.User;
import infra.view.Image;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class EntidadeHierarquiaView extends PortalView {
	private Entidade entidade, origemMenu;

	public EntidadeHierarquiaView(Entidade entidade, Entidade origemMenu)
			throws Exception {
		this.entidade = entidade;
		this.origemMenu = origemMenu;
	}

	private void adicionarEntidade(Entidade entidade, int nivel,
			boolean selecionado, Table table) throws Exception {
		Table t = new Table(5);
		t.addStyle(Table.STYLE_BLANK);
		t.add(new Space(nivel * 4));
		t.add(new Image(entidade.obterIcone()));
		t.add(new Space());
		Label label;
		label = new Label(entidade.obterNome());

		label.setBold(selecionado);
		Link link = new Link(label, new Action("visualizarHierarquiaEntidade"));
		link.getAction().add("id", entidade.obterId());
		link.getAction().add("_entidadeId", entidade.obterId());
		t.add(link);
		table.addData(t);

		Label codigoLabel = new Label("");

		if (entidade instanceof Conta) {
			Conta conta = (Conta) entidade;
			codigoLabel = new Label(conta.obterCodigo());
			codigoLabel.setBold(selecionado);
		} else if (entidade instanceof ClassificacaoContas) {
			ClassificacaoContas cContas = (ClassificacaoContas) entidade;
			codigoLabel = new Label(cContas.obterCodigo());
			codigoLabel.setBold(selecionado);
		}

		table.addData(codigoLabel);

		Label tipo = new Label(entidade.obterDescricaoClasse());
		tipo.setBold(selecionado);
		table.addData(tipo);

		Usuario responsavel = entidade.obterResponsavel();
		Label responsavelLabel = new Label("");
		if (responsavel != null)
			responsavelLabel.setValue(responsavel.obterNome());
		responsavelLabel.setBold(selecionado);
		table.addData(responsavelLabel);

	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		Table table = new Table(4);
		table.setWidth("100%");

		table.addHeader("Nombre");
		table.addHeader("Cuenta");
		table.addHeader("Tipo");
		table.addHeader("Responsable");

		// obter nós superiores

		Collection superiores = new ArrayList();
		Entidade e = this.entidade.obterSuperior();
		while (e != null) {
			superiores.add(e);
			e = e.obterSuperior();
		}

		// mostrar nós superiores
		int nivel = 0;
		Object[] array = superiores.toArray();
		for (int i = array.length - 1; i >= 0; i--) {
			e = (Entidade) array[i];
			this.adicionarEntidade(e, nivel++, false, table);
		}

		// mostrar nó
		this.adicionarEntidade(this.entidade, nivel++, true, table);

		// mostrar nós inferiores

		Map mapInferiores = new TreeMap();

		for (Iterator i = this.entidade.obterInferiores().iterator(); i
				.hasNext();) {
			Entidade e2 = (Entidade) i.next();
			if (e2 instanceof ClassificacaoContas) {
				ClassificacaoContas cContas = (ClassificacaoContas) e2;
				System.out.println("Codigo: " + cContas.obterCodigo());
				System.out.println("Id: " + cContas.obterId());
				mapInferiores.put(cContas.obterCodigo(), e2);
			} else if (e2 instanceof Conta) {
				Conta conta = (Conta) e2;
				mapInferiores.put(conta.obterCodigo(), e2);
			} else
				mapInferiores.put(e2.obterNome(), e2);
		}

		for (Iterator i = mapInferiores.values().iterator(); i.hasNext();) {
			Entidade e2 = (Entidade) i.next();
			this.adicionarEntidade(e2, nivel, false, table);
		}

		return table;
	}

	public String getSelectedGroup() throws Exception {
		return this.entidade.obterNome();
	}

	public String getSelectedOption() throws Exception {
		return "Jerarquía";
	}

	public View getTitle() throws Exception {
		return new Label("Jerarquía");
	}

	public Entidade getOrigemMenu() throws Exception {
		if (this.origemMenu.obterId() != 0)
			return this.origemMenu;
		else
			return this.entidade;
	}
}