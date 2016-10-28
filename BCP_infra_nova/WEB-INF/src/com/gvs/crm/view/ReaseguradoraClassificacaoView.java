package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.NivelClassificacaoSelect;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Reaseguradora;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class ReaseguradoraClassificacaoView extends PortalView {

	private Reaseguradora reaseguradora;

	private Reaseguradora.Classificacao classificacao;

	private boolean novo;

	public ReaseguradoraClassificacaoView(Reaseguradora reaseguradora)
			throws Exception {
		this.reaseguradora = reaseguradora;
		this.novo = true;
	}

	public ReaseguradoraClassificacaoView(
			Reaseguradora.Classificacao classificacao) throws Exception {
		this.reaseguradora = classificacao.obterReaseguradora();
		this.classificacao = classificacao;
		this.novo = false;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		Table table = new Table(2);

		if (this.novo) {
			table.addHeader("Calificación:");
			table.addData(new InputString("classificacao", null, 50));

			table.addHeader("Nivel:");

			Block block = new Block(Block.HORIZONTAL);
			block.add(new NivelClassificacaoSelect("nivel", this.reaseguradora,
					""));
			block.add(new Space(2));
			block.add(new InputString("novoNivel", "", 50));

			table.add(block);

			table.addHeader("Cod. Calificadora:");
			table.addData(new InputString("codigo", null, 15));

			table.addHeader("Calificadora:");
			table.addData(new InputString("qualificacao", null, 50));

			table.addHeader("Fecha de la Calificación:");
			table.addData(new InputDate("data", null));

			Action incluirAction = new Action("incluirClassificacao");
			incluirAction.add("entidadeId", this.reaseguradora.obterId());
			Button incluirButton = new Button("Agregar", incluirAction);
			table.addFooter(incluirButton);
		} else {
			table.addHeader("Calificación:");
			table.addData(new InputString("classificacao", this.classificacao
					.obterClassificacao(), 50));

			table.addHeader("Nivel:");
			Block block = new Block(Block.HORIZONTAL);
			block.add(new NivelClassificacaoSelect("nivel", this.reaseguradora,
					this.classificacao.obterNivel()));
			block.add(new Space(2));
			block.add(new InputString("novoNivel", "", 50));

			table.add(block);

			table.addHeader("Cod. Calificadora:");
			table.addData(new InputString("codigo", this.classificacao
					.obterCodigo(), 15));

			table.addHeader("Calificadora:");
			table.addData(new InputString("qualificacao", this.classificacao
					.obterQualificacao(), 50));

			table.addHeader("Fecha de la Calificación:");
			table
					.addData(new InputDate("data", this.classificacao
							.obterData()));

			Action atualizarAction = new Action("atualizarClassificacao");
			atualizarAction.add("entidadeId", this.reaseguradora.obterId());
			atualizarAction.add("id", this.classificacao.obterId());
			Button atualizarButton = new Button("Actualizar", atualizarAction);
			table.addFooter(atualizarButton);

			Action excluirAction = new Action("excluirClassificacao");
			excluirAction.add("entidadeId", this.reaseguradora.obterId());
			excluirAction.add("id", this.classificacao.obterId());
			excluirAction.setConfirmation("Confirma exclusión ?");
			Button excluirButton = new Button("Eliminar", excluirAction);
			table.addFooter(excluirButton);
		}

		Action cancelarAction = new Action("visualizarDetalhesEntidade");
		cancelarAction.add("id", this.reaseguradora.obterId());
		table.addFooter(new Button("Volver", cancelarAction));
		return table;
	}

	public String getSelectedGroup() throws Exception {
		return null;
	}

	public String getSelectedOption() throws Exception {
		return null;
	}

	public View getTitle() throws Exception {
		if (this.novo)
			return new Label("Nueva Calificación");
		else
			return new Label("Calificación - "
					+ this.classificacao.obterClassificacao());
	}

	public Entidade getOrigemMenu() throws Exception {
		return null;
	}
}