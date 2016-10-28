package com.gvs.crm.view;

import java.util.Iterator;

import com.gvs.crm.model.Pessoa;

import infra.control.Action;
import infra.view.Button;
import infra.view.Image;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;

public class PessoaFormacoesView extends Table {
	public PessoaFormacoesView(Pessoa pessoa) throws Exception {
		super(4);

		this.addStyle(super.STYLE_ALTERNATE);

		this.setWidth("80%");

		this.add("");
		this.addHeader("Institución");
		this.addHeader("Curso");
		this.addHeader("Tipo Formación");

		for (Iterator i = pessoa.obterFormacoes().iterator(); i.hasNext();) {
			Pessoa.Formacao formacao = (Pessoa.Formacao) i.next();

			if (pessoa.permiteAtualizar()) {
				Action excluirAction = new Action("excluirFormacao");
				excluirAction.add("entidadeId", formacao.obterPessoa()
						.obterId());
				excluirAction.add("id", formacao.obterId());
				excluirAction
						.setConfirmation("Confirma exclusión del Direccione ?");
				this.add(new Link(new Image("delete.gif"), excluirAction));
			} else
				this.add("");

			Link link = new Link(formacao.obterInstituicao(), new Action(
					"visualizarFormacao"));
			link.getAction()
					.add("entidadeId", formacao.obterPessoa().obterId());
			link.getAction().add("id", formacao.obterId());

			this.add(link);
			this.add(formacao.obterCurso());
			this.add(formacao.obterTipo());
		}

		this.setNextColSpan(this.getColumns());
		this.add(new Space());

		Button novoButton = new Button("Nueva", new Action("novaFormacao"));
		novoButton.getAction().add("entidadeId", pessoa.obterId());
		this.setNextColSpan(this.getColumns());
		this.add(novoButton);
	}
}