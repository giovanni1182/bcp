package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Iterator;

import com.gvs.crm.model.Reaseguradora;

import infra.control.Action;
import infra.view.Button;
import infra.view.Image;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;

public class ReaseguradoraClassificacoesView extends Table {
	public ReaseguradoraClassificacoesView(Reaseguradora reaseguradora)
			throws Exception {
		super(7);

		this.addStyle(super.STYLE_ALTERNATE);

		this.addSubtitle("");

		this.setWidth("80%");

		this.add("");
		this.add("");
		this.addHeader("Calificación");
		this.addHeader("Nivel");
		this.addHeader("Cod. Calificadora");
		this.addHeader("Calificadora");
		this.addHeader("Fecha de la Calificación");

		for (Iterator i = reaseguradora.obterClassificacoes().iterator(); i
				.hasNext();) {
			Reaseguradora.Classificacao classificacao = (Reaseguradora.Classificacao) i
					.next();

			if (reaseguradora.permiteAtualizar()) {
				Action visualizarAction = new Action("visualizarClassificacao");
				visualizarAction.add("entidadeId", reaseguradora.obterId());
				visualizarAction.add("id", classificacao.obterId());
				this.add(new Link(new Image("replace.gif"), visualizarAction));

				Action excluirAction = new Action("excluirClassificacao");
				excluirAction.add("entidadeId", reaseguradora.obterId());
				excluirAction.add("id", classificacao.obterId());
				excluirAction.setConfirmation("Confirma exclusión ?");
				this.add(new Link(new Image("delete.gif"), excluirAction));
			} else {
				this.add("");
				this.add("");
			}

			this.add(classificacao.obterClassificacao());
			this.add(classificacao.obterNivel());
			this.add(classificacao.obterCodigo());
			this.add(classificacao.obterQualificacao());

			String data = new SimpleDateFormat("dd/MM/yyyy")
					.format(classificacao.obterData());

			this.add(data);
		}

		this.setNextColSpan(this.getColumns());
		this.add(new Space());

		Button novoLink = new Button("Nueva", new Action("novaClassificacao"));
		novoLink.getAction().add("entidadeId", reaseguradora.obterId());
		this.setNextColSpan(this.getColumns());
		this.add(novoLink);
	}
}