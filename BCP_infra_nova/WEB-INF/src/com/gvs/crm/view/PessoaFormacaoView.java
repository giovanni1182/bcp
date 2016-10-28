package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.TipoEducacaoSelect;
import com.gvs.crm.component.TipoFormacaoSelect;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Pessoa;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.InputString;
import infra.view.InputText;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class PessoaFormacaoView extends PortalView {
	private Pessoa pessoa;

	private Pessoa.Formacao formacao;

	private boolean novo;

	public PessoaFormacaoView(Pessoa pessoa) throws Exception {
		this.pessoa = pessoa;
		this.novo = true;
	}

	public PessoaFormacaoView(Pessoa.Formacao formacao) throws Exception {
		this.pessoa = formacao.obterPessoa();
		this.formacao = formacao;
		this.novo = false;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		Table table = new Table(2);

		if (this.novo) {
			table.addHeader("Tipo de la Formación:");
			table.add(new TipoFormacaoSelect("tipo", ""));

			table.addHeader("Tipo de la Educación:");
			table.add(new TipoEducacaoSelect("tipoEducacao", ""));

			table.addHeader("Nombre de la Instituición:");
			table.add(new InputString("instituicao", null, 70));

			table.addHeader("Curso:");
			table.add(new InputString("curso", null, 70));

			table.addHeader("Carga Horaria:");
			table.add(new InputString("carga", null, 20));

			table.addHeader("Fecha Inicio:");
			table.add(new InputDate("inicio", null));

			table.addHeader("Fecha Finalización:");
			table.add(new InputDate("fim", null));

			table.addHeader("Experiencia Profesional:");
			table.add(new InputText("experiencia", null, 5, 80));

			Action incluirAction = new Action("incluirFormacao");
			incluirAction.add("entidadeId", this.pessoa.obterId());
			Button incluirButton = new Button("Agregar", incluirAction);
			//incluirButton.setEnabled(pessoa.permiteAtualizar());
			table.addFooter(incluirButton);
		} else {

			table.addHeader("Tipo de la Formación:");
			table
					.add(new TipoFormacaoSelect("tipo", this.formacao
							.obterTipo()));

			table.addHeader("Tipo de la Educación:");
			table.add(new TipoEducacaoSelect("tipoEducacao", this.formacao
					.obterTipoEducacao()));

			table.addHeader("Nombre de la Instituición:");
			table.add(new InputString("instituicao", this.formacao
					.obterInstituicao(), 20));

			table.addHeader("Curso:");
			table.add(new InputString("curso", this.formacao.obterCurso(), 20));

			table.addHeader("Carga Horaria:");
			table.add(new InputString("carga", this.formacao
					.obterCargaHoraria(), 20));

			table.addHeader("Fecha Inicio:");
			table.add(new InputDate("inicio", this.formacao.obterDataInicio()));

			table.addHeader("Fecha Finalización:");
			table.add(new InputDate("fim", this.formacao.obterDataFim()));

			table.addHeader("Experiencia Profesional:");
			table.add(new InputText("experiencia", this.formacao
					.obterExperiencia(), 5, 80));

			Action atualizarAction = new Action("atualizarFormacao");
			atualizarAction.add("entidadeId", this.pessoa.obterId());
			atualizarAction.add("id", this.formacao.obterId());
			Button atualizarButton = new Button("Actualizar", atualizarAction);
			atualizarButton.setEnabled(pessoa.permiteAtualizar());
			table.addFooter(atualizarButton);

			Action excluirAction = new Action("excluirFormacao");
			excluirAction.add("entidadeId", this.pessoa.obterId());
			excluirAction.add("id", this.formacao.obterId());
			excluirAction.setConfirmation("Confirma exclusión ?");
			Button excluirButton = new Button("Eliminar", excluirAction);
			excluirButton.setEnabled(pessoa.permiteExcluir());
			table.addFooter(excluirButton);
		}
		Action cancelarAction = new Action("visualizarDetalhesEntidade");
		cancelarAction.add("id", this.pessoa.obterId());
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
			return new Label(this.pessoa.obterNome() + " - Nueva Formación");
		else
			return new Label(this.pessoa.obterNome() + "- Formación");
	}

	public Entidade getOrigemMenu() throws Exception {
		return null;
	}
}