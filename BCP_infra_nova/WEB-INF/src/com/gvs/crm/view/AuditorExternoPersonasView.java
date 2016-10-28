package com.gvs.crm.view;

import java.util.Iterator;

import com.gvs.crm.model.AuditorExterno;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Pessoa;

import infra.control.Action;
import infra.view.Button;
import infra.view.Image;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;

public class AuditorExternoPersonasView extends Table {
	public AuditorExternoPersonasView(AuditorExterno auditor) throws Exception {
		super(5);

		this.addStyle(super.STYLE_ALTERNATE);
		this.setWidth("80%");

		this.add("");
		this.addHeader("Nombre");
		this.addHeader("Tipo de Responsabilidad");
		this.addHeader("Tipo de Persona");
		this.addHeader("Cargo");

		for (Iterator i = auditor.obterInferiores().iterator(); i.hasNext();) {
			Entidade e = (Entidade) i.next();

			if (e instanceof Pessoa) {
				Pessoa pessoa = (Pessoa) e;

				if (auditor.permiteAtualizar()) {
					Action excluirAction = new Action("excluirEntidade");
					excluirAction.add("id", pessoa.obterId());
					excluirAction.setConfirmation("Confirma exclusión ?");
					this.add(new Link(new Image("delete.gif"), excluirAction));
				} else
					this.add("");

				Link link = new Link(e.obterNome(), new Action(
						"visualizarDetalhesEntidade"));
				link.getAction().add("id", pessoa.obterId());

				this.add(link);

				Pessoa.Atributo tipo = (Pessoa.Atributo) pessoa
						.obterAtributo("tipo");

				String tipoStr = "";

				if (tipo != null)
					tipoStr = tipo.obterValor();

				Pessoa.Atributo tipoResponsabilidade = (Pessoa.Atributo) pessoa
						.obterAtributo("tiporesponsabilidade");

				String tipoResponsabilidadeStr = "";

				if (tipoResponsabilidade != null)
					tipoResponsabilidadeStr = tipoResponsabilidade.obterValor();

				Pessoa.Atributo cargo = (Pessoa.Atributo) pessoa
						.obterAtributo("cargo");

				String cargoStr = "";

				if (cargo != null)
					cargoStr = cargo.obterValor();

				this.add(tipoResponsabilidadeStr);
				this.add(tipoStr);
				this.add(cargoStr);
			}
		}

		this.setNextColSpan(this.getColumns());
		this.add(new Space());

		Button novoLink = new Button("Nueva", new Action("novaEntidade"));
		novoLink.getAction().add("superiorId", auditor.obterId());
		novoLink.getAction().add("classe", "pessoa");
		this.setNextColSpan(this.getColumns());
		this.add(novoLink);
	}
}